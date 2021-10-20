package com.example.registration_form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class mess_finding extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dataRef;
    private OkHttpClient client;

    private String url;

    private ProgressDialog progressDialog;

    private Button search,clear;
    private EditText address;
    private TextView result1;
    private List<info> listinfo;

    private Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_finding);
//        getActionBar().setIcon(R.drawable.logo);
        setTitle("Mess Recommender");
//        getActionBar().setIcon(R.drawable.logo);
//        getActionBar().setHomeButtonEnabled(true);

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference("Registered user");
        client = new OkHttpClient();
        progressDialog = new ProgressDialog(mess_finding.this);
        progressDialog.setMessage("Loading");

        listinfo = new ArrayList<info>();

        clear = findViewById(R.id.Clear);
        search = findViewById(R.id.SEARCH);
        address = findViewById(R.id.add);
        result1 = findViewById(R.id.RESULT1);


        result1.setMovementMethod(new ScrollingMovementMethod());

        progressDialog = new ProgressDialog(mess_finding.this);
        progressDialog.setMessage("Loading...");

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

//      Code to find latitude and longitude of entered address

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Code to find latitude and longitude of the address
                url = "https://api.opencagedata.com/geocode/v1/json?q="+ address.getText().toString() +",india&key=f9d4c3ced3904ebd9f05802bd651534b";
                new myLatsLongs().execute();

            }
        });


    }

    public HashMap<info,Double> sortbyval(HashMap<info,Double> hm){
        List<Map.Entry<info, Double> > list = new LinkedList<Map.Entry<info, Double> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<info, Double> >() {
            public int compare(Map.Entry<info, Double> o1,
                               Map.Entry<info, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        HashMap<info, Double> temp = new LinkedHashMap<info, Double>();
        for (HashMap.Entry<info, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public double distance(double lat1,double lng1,double lat2,double lng2){
        double theta = lng1 - lng2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2)) + Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));
        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515;

        return dist * 1.609344;
    }

    public double degToRad(double deg){
        return (deg * Math.PI/180.0);
    }

    public double radToDeg(double rad){
        return (rad *180.0/Math.PI);
    }


    class myLatsLongs extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Request request = new Request.Builder().url(url).build();
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


//            Code to fetch the data from database and storing it into list in java
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for (DataSnapshot child : children) {
                        info value = child.getValue(info.class);
                        listinfo.add(value);
                    }


                    try {
                        JSONObject obj = new JSONObject(response.body().string());

                        JSONArray arr = obj.getJSONArray("results");

                        JSONObject ob1 = arr.getJSONObject(0);

                        JSONObject ob12 = ob1.getJSONObject("geometry");
                        Double lati = Double.parseDouble((String) ob12.getString("lat"));
                        Double longi = Double.parseDouble((String)ob12.getString("lng"));

                        result1.setText(listinfo.get(0).getMess());

                        HashMap<info,Double> hashMap = new HashMap<>();
                        DecimalFormat df = new DecimalFormat("0.00");
                        for (info i:listinfo){
                            hashMap.put(i,Double.parseDouble(df.format(distance(Double.parseDouble(i.getLatitude()),Double.parseDouble(i.getLongitude()),lati,longi))));
                        }
                        hashMap = sortbyval(hashMap);
                        String s="";
                        for(Map.Entry<info, Double> a:hashMap.entrySet()){
                            s+="\nDistance - "+a.getValue().toString()+" km"+"\n"+a.getKey().getDetails()+"\n";
                        }
                        result1.setText(s);
                        s = "";
                    }
                    catch(JSONException | IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            progressDialog.dismiss();
        }

    }
}

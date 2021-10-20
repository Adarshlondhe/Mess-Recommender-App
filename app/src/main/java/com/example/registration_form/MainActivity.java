package com.example.registration_form;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {


    // Write a message to the database
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Registered user");
    private ProgressDialog progressDialog;
    private EditText Mess;
    private EditText Add1;
    private EditText Add2;
    private EditText Cuisine;
    private EditText Description;
    private EditText City;
    private EditText State;
    private EditText Phone;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);



        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Registering your mess....");

        Mess = findViewById(R.id.MESS);
        Add1 = findViewById(R.id.ADD1);
        Add2 = findViewById(R.id.ADD2);
        Cuisine = findViewById(R.id.CUISINE);
        Description = findViewById(R.id.DESC);
        City = findViewById(R.id.CITY);
        State = findViewById(R.id.STATE);
        Phone = findViewById(R.id.PHONE);
        Register = findViewById(R.id.REGISTER);

        info i =new info();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//              Code to find latitude and longitude of entered address
                progressDialog.show();

                OkHttpClient client = new OkHttpClient();
                String add1 = Add1.getText().toString();
                String add2 = Add2.getText().toString();
                String city = City.getText().toString();
                String state = State.getText().toString();

                String url = "https://api.opencagedata.com/geocode/v1/json?q="+ (add1+","+add2+","+city+","+state+",") +"india&key=f9d4c3ced3904ebd9f05802bd651534b";

                Request request = new Request.Builder().url(url).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String s = response.body().string();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(s);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JSONArray arr = null;
                                    try {
                                        arr = (JSONArray) jsonObject.get("results");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JSONObject ob1 = null;
                                    try {
                                        ob1 = (JSONObject) arr.get(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JSONObject ob12 = null;
                                    try {
                                        ob12 = (JSONObject) ob1.get("geometry");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Double lat = null;
                                    try {
                                        lat = (Double) ob12.get("lat");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Double longi = null;
                                    try {
                                        longi = (Double) ob12.get("lng");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                    Inserting into the database
                                    info i = new info();
                                    String mess = Mess.getText().toString();
                                    String add1 = Add1.getText().toString();
                                    String add2 = Add2.getText().toString();
                                    String description = Description.getText().toString();
                                    String cuisine = Cuisine.getText().toString();
                                    String city = City.getText().toString();
                                    String state = State.getText().toString();
                                    String phone = Phone.getText().toString();
                                    i.setLongitude(longi.toString());
                                    i.setLatitude(lat.toString());
                                    i.setMess(mess);
                                    i.setAdd2(add2);
                                    i.setAdd1(add1);
                                    i.setCuisine(cuisine);
                                    i.setDescription(description);
                                    i.setCity(city);
                                    i.setState(state);
                                    i.setPhone(phone);

                                    myRef.push().setValue(i);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                });




                Intent intent = new Intent(MainActivity.this,mess_registration_success.class);
                startActivity(intent);
            }
        });
    }
}


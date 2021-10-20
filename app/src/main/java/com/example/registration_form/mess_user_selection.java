package com.example.registration_form;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mess_user_selection extends AppCompatActivity {
    Button finder ;
    Button register ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_user_selection);

        setTitle("Mess Recommender");
        finder = (Button) findViewById(R.id.FINDING);
        register = (Button) findViewById(R.id.REGISTRATION);

        finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mess_user_selection.this,mess_finding.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mess_user_selection.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
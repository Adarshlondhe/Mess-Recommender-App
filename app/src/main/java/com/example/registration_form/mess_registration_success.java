package com.example.registration_form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class mess_registration_success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_registration_success);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(mess_registration_success.this,mess_user_selection.class);
                    startActivity(intent);
                }
            }
        };thread.start();
    }
}
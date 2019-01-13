package com.hackathon.hackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;

import java.util.Timer;
import java.util.TimerTask;

public class OrganizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
         final EditText test = (EditText)findViewById(R.id.test);

        

        Timer time =new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        test.setText(""+Math.random());
                    }
                });
            }
        }, 1000, 1000); // does this task every second
    }




}

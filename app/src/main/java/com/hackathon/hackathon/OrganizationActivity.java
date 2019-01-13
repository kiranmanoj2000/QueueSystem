package com.hackathon.hackathon;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrganizationActivity extends AppCompatActivity {


    private int queueSize = 0;
    int lineNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

         FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(0);
         FirebaseDatabase.getInstance().getReference("CurrentQueueSize").setValue(0);


        // when the current queue changes update the UI
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = (long)dataSnapshot.getValue();
                // if the number is zero
                if(number==0){
                    TextView displayNum = (TextView)findViewById(R.id.textView);
                    displayNum.setText("No More Customers");
                }else{
                    TextView displayNum = (TextView)findViewById(R.id.textView);
                    displayNum.setText(String.valueOf(number));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("CurrentQueueSize").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size = (long)dataSnapshot.getValue();
                TextView sizeQueue = (TextView)findViewById(R.id.numQueue);
                sizeQueue.setText(size+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // will be called when a customer leaves
    public void moveToNextInLine(View view) {
        ++lineNum;
        ++queueSize;
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(lineNum);
        // increase the size

    }

    // for closing up shop
    public void closeQueueing(View view){
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(0);

    }

    // when








}

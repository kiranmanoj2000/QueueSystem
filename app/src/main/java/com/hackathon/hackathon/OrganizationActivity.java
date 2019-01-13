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


         final EditText test = (EditText)findViewById(R.id.test);

         ArrayList<String> tests = new ArrayList<>();
         tests.add("Hello");
         tests.add("arrau");
         tests.add("stack");
         FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(1);
         FirebaseDatabase.getInstance().getReference("User2").setValue(2);
         FirebaseDatabase.getInstance().getReference("User3").setValue(3);
         FirebaseDatabase.getInstance().getReference("User4").setValue(tests);
         JobScheduler scheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);

         ComponentName service = new ComponentName(this, JobSchedulerService.class);
         JobInfo inf = new JobInfo.Builder(2, service).setMinimumLatency(1000).build();
         scheduler.schedule(inf);

        FirebaseDatabase.getInstance().getReference("CurrentQueue").addValueEventListener(new ValueEventListener() {

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

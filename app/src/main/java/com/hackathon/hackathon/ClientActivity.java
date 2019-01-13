package com.hackathon.hackathon;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClientActivity extends AppCompatActivity {

    private TextView mValueView;

    private long clientID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // setting the position of the client in the queue
        FirebaseDatabase.getInstance().getReference("CurrentQueueSize").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 clientID= (long)(dataSnapshot.getValue())+1;
                TextView displayNum = (TextView)findViewById(R.id.textView);
                displayNum.setText(String.valueOf(clientID));
                // increase the size of the queue
                FirebaseDatabase.getInstance().getReference("CurrentQueueSize").setValue(clientID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // save the spot of the client
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = (long)dataSnapshot.getValue();
                TextView displayNum = (TextView)findViewById(R.id.textView);
                displayNum.setText(String.valueOf(number));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });



        //checkInBackground();
    }

    // method to schedule job to check if it is the customers turn in line
    public void checkInBackground(){
        // adding a boolean to let the job know if its work is done
        PersistableBundle isTurn = new PersistableBundle();
        isTurn.putLong("clientID", clientID);
        // scheduling the job
        JobScheduler scheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        ComponentName service = new ComponentName(this, JobSchedulerService.class);
        // creating the info of the job with specific guidlines
        // checks for updates every 10 seconds, for a max of 5 hours
        JobInfo inf = new JobInfo.Builder(2, service).setExtras(isTurn).build();
        scheduler.schedule(inf);
    }






}

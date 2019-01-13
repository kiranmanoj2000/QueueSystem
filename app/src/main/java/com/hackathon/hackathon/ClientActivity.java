package com.hackathon.hackathon;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ClientActivity extends AppCompatActivity {

    private boolean hasSignedIn = false;
    private long clientID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);






    }

    public void FireBaseListener(){
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
                TextView displayCurrentlyCalling = (TextView)findViewById(R.id.clientLineNum);
                displayCurrentlyCalling.setText(number+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // method to schedule job to check if it is the customers turn in line
    public void checkInBackground(){
        // adding a boolean to let the job know if its work is done
        PersistableBundle isTurn = new PersistableBundle();
        isTurn.putLong("clientID", clientID);
        // scheduling the job
        JobScheduler scheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        ComponentName service = new ComponentName(this, JobSchedulerService.class);
        // creating the info of the job with specific guidelines
        // checks for updates every 10 seconds, for a max of 5 hours

        JobInfo inf = new JobInfo.Builder((int)(Math.random()*101), service).setExtras(isTurn).setMinimumLatency(1000).build();
        scheduler.schedule(inf);
    }

    final Activity activity = this;

    public void openCamera(View view){
        if(!hasSignedIn){
            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan QR Code");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // only let them do this once
        if(!hasSignedIn){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if (result.getContents() == null||!result.getContents().equals(WelcomActivity.code+"")) {
                    Toast.makeText(this, "Scan Failed/Wrong Code", Toast.LENGTH_LONG).show();
                } else {
                    hasSignedIn = true;
                    Toast.makeText(this, "Scan Successful!   " +result.getContents(), Toast.LENGTH_LONG).show();
                    FireBaseListener();
                    checkInBackground();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        }

        public void calculateTimeEst(View view){

            final int currentInLine = 0;
            FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int currentInLine  = (int)dataSnapshot.getValue();
                    TextView timeUntil = (TextView)findViewById(R.id.timeEstimate);
                    timeUntil.setText(""+(clientID-currentInLine)*10);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

}

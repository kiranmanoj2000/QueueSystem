package com.hackathon.hackathon;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrganizationActivity extends AppCompatActivity {


    private int queueSize = 0;
    int lineNum = 1;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        // code for QR code


        image = (ImageView) findViewById(R.id.image);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(""+WelcomActivity.code, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }



        // when the current queue changes update the UI
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = (long)dataSnapshot.getValue();
                // if the queue is empty

                if(number==0){
                    TextView displayNum = (TextView)findViewById(R.id.textView);
                    displayNum.setText("0");
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
        // only move move to the next client if they queue allows
        if(lineNum<=queueSize && lineNum!=2){
            FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(lineNum);

        }else{
            FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(lineNum-1);

        }
        // increase the size

    }

    // for closing up shop
    public void closeQueueing(View view){
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(0);
        FirebaseDatabase.getInstance().getReference("CurrentQueueSize").setValue(0);

    }

    // when








}

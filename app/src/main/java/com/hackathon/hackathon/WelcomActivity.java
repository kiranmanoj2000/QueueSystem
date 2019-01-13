package com.hackathon.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        FirebaseDatabase.getInstance().getReference("CurrentQueueInLine").setValue(0);
        FirebaseDatabase.getInstance().getReference("CurrentQueueSize").setValue(0);
    }

    // method to move to organization screen
    public void moveToOrganization(View view){
        Intent intent = new Intent(this, OrganizationActivity.class);
        startActivity(intent);
    }

    // method to move to organization screen
    public void moveToClient(View view){
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }
}

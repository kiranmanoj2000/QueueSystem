package com.hackathon.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
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

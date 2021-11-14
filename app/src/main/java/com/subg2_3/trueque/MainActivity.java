package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToAddProduct (View view) {
        Intent intentAdd = new Intent(this, AddActivity.class);
        startActivity(intentAdd);
    }

    public void goToAddProductProfile (View view) {
        Intent intentPProfile = new Intent(this, ProductProfile.class);
        startActivity(intentPProfile);
    }
}
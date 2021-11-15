package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ProductProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_profile);
    }

    public void goToMain(View view){
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}
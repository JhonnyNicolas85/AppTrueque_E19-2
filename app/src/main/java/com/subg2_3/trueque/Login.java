package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToCheckIn(View view){
        Intent intentCheckIn = new Intent(this, CheckIn.class);
        startActivity(intentCheckIn);
    }
}
package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
//Cambio la pantalla de inicio por la main activity, ya que en esta esta la condicion de si ya inicio
//sesión o no
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class );
                startActivity(intent);
                finish();
            }
        };
        Timer timeDelay =new Timer();
        timeDelay.schedule(tarea, 3000);

    }
}
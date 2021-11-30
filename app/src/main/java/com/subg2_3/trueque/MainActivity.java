package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        CerrarSesion = findViewById(R.id.CerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Creamos un metodo para cerrar sesion*/
                CerrarSesion();
            }
        });

    }

    /*Realizamos un onstart*/

    @Override
    protected void onStart() {
        /*Llamamos al metodo para que se ejecute cuando inicie el activity*/
        verificasionInicioSesion();
        super.onStart();
    }


    /* Creamos el metodo que nos permita saber si el usuario ha iniciado sesión antes*/

    private void  verificasionInicioSesion(){
        /*Si el usuari ya ha iniciado sesión antes nos dirige la main activity*/
        if (firebaseUser != null ){
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_LONG).show();
        }
        /*En caso de no haber iniciaso sesion antes nos lleva al login*/
        else{
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
    }

    /*Metodo para cerrar sesion*/
    private void CerrarSesion(){
        firebaseAuth.signOut();/*Cierra la sesion del ususario que este activo en la app*/
        Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_LONG).show();
        /*Luego de cerrar sesion que nos dirija al login*/
        startActivity(new Intent(MainActivity.this,Login.class));
    }

    public void goToAddProduct (View view) {
        Intent intentAdd = new Intent(this, AddActivity.class);
        startActivity(intentAdd);
    }

    public void goToAddProductProfile (View view) {
        Intent intentPProfile = new Intent(this, ProductProfile.class);
        startActivity(intentPProfile);
    }
    public void goToMain(View view){
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}
package com.subg2_3.trueque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.subg2_3.trueque.Opciones.Mis_datos;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Button CerrarSesion, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        CerrarSesion = findViewById(R.id.CerrarSesion);

        btnProfile = findViewById(R.id.btnProfile);


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Mis_datos.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Creamos un metodo para cerrar sesion*/
                CerrarSesion();

                googleSignInClient.signOut();
                startActivity(new Intent(MainActivity.this,Login.class));
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

    public void gotoProfilePerson(View view){
        Intent intentProfile = new Intent(this,profileperson.class);
        startActivity(intentProfile);
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
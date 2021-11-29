package com.subg2_3.trueque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText etUser, etPassword;
    Button loginBtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.loginBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this); /*Inicializamos el progressdialog*/
        dialog = new Dialog(Login.this); /*Inicializamos el dialog*/



        //Asignamos un evento al boton ingresar
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Convertimos a string el correo y la contraseña

                String correo = etUser.getText().toString();
                String pass = etPassword.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){

                    etUser.setError("Correo invalido");
                    etUser.setFocusable(true);

                }else if (pass.length()<6){

                    etPassword.setError("La contraseña debe ser mayor o igual a seis caracteres");
                    etPassword.setFocusable(true);

                }else {

                    //Se ejecuta el metodo
                    LOGINUSUARUIO(correo, pass);

                }

            }
        });
    }

    /*Metodo para loggear usuario*/
    private void LOGINUSUARUIO(String correo, String pass) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Si se incia sesion correctamente entonces...
                        if (task.isSuccessful()){

                            progressDialog.dismiss();//El progress se cierra

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Al iniciar sesion nos mande a la actividad main
                            startActivity(new Intent(Login.this, MainActivity.class));
                            assert user != null; // afirmamos que el usuario no sea nulo, obtenemos su correo electronico
                            Toast.makeText(Login.this, "Hola! bienvenido(a) "+ user.getEmail(), Toast.LENGTH_LONG).show();
                            finish();

                        }else {

                            progressDialog.dismiss();
                            //Al tener mas las credenciales, se mostrara el dialog que creamos
                            Dialog_No_Inicio();
                            //Toast.makeText(Login.this, "Algo ha salido mal", Toast.LENGTH_LONG).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                //que nos muestre el mensaje
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*Crear el dialog personalizado*/

    private void Dialog_No_Inicio(){

        Button ok_no_inicio;
        dialog.setContentView(R.layout.no_sesion);/*Hacemos la conexion con nuestra vista creada*/

        ok_no_inicio = dialog.findViewById(R.id.ok_no_inicio);

        /*al precionar el boton entendido se cierra el cuadro de dialogo*/
        ok_no_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);/*Al precionar fuera de la animación esta seguira mostrandose, hasta que se precione el boton entendido*/
        dialog.show();

    }


    public void goToCheckIn(View view){
        Intent intentCheckIn = new Intent(this, CheckIn.class);
        startActivity(intentCheckIn);
    }

}
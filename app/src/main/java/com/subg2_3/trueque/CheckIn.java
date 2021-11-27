package com.subg2_3.trueque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CheckIn extends AppCompatActivity {

    EditText etEmail,etPass, etName, etPhone;
    Button btnRegister;

    /*Declaramos firebase auth para la verificacion de nuestros ususarios*/
    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        /*Realizamos la conexion de nuestros TV con el checking.java*/
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);

        /*Creamos la instancia de firebaseAuth*/
        firebaseAuth = FirebaseAuth.getInstance();

        /*Creamos un evento para el botoón registrar, que al momento de precionarlo realiece una accion*/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();

                /*Validamos que el correo debe llevar @ y la contraseña sea mayor a 6 caracteres*/

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    etEmail.setError("Correo no valido");
                    etEmail.setFocusable(true);

                }else if (pass.length()<6){

                    etPass.setError("La contraseña debe tener mas de 6 caracteres o no son iguales");
                    etPass.setFocusable(true);


                }else{

                    Registrar(email,pass);


                }

            }
        });
    }

    /*Crear usuario con correo y contraseña*/
    private void Registrar(String email, String pass) {

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*Si el registro es exitoso */
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            /*Ponemos los datos que queremos registrar*/
                            assert user != null;/*Afirmamos que el usuario no sea nulo*/
                            String uid = user.getUid();/*Para obtener el uid */
                            String email = etEmail.getText().toString();
                            String pass = etPass.getText().toString();
                            String name = etName.getText().toString();
                            String phone = etPhone.getText().toString();

                            /*Creamos un hashmap para enviar los datos a firebase*/

                            HashMap<Object,String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("uid",uid);
                            DatosUsuario.put("email",email);
                            DatosUsuario.put("pass",pass);
                            DatosUsuario.put("name",name);
                            DatosUsuario.put("phone",phone);
                            /*Dejo la imagen vacia*/
                            DatosUsuario.put("image","");

                            /*Iniciamos la instancia a la base de datos*/
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            /*Creamos la base de datos*/
                            DatabaseReference reference = database.getReference("Usuarios_de_la_app");
                            /*El nombre de la base de datos es Ususarios_de_la_app*/
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(CheckIn.this, "Se registro exitosamente", Toast.LENGTH_LONG).show();
                            /*Una vez se haya registrado sera llevado directamente al main*/
                            startActivity(new Intent(CheckIn.this, MainActivity.class));

                        }else {
                            Toast.makeText(CheckIn.this, "Algo ha salido mal", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheckIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void goToLogin(View view){
        Intent intentLogin = new Intent(this, Login.class);
        startActivity(intentLogin);
    }
}
package com.subg2_3.trueque;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText etUser, etPassword;
    Button loginBtn, ingresargoogle;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

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
        ingresargoogle = findViewById(R.id.ingresargoogle);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this); /*Inicializamos el progressdialog*/
        dialog = new Dialog(Login.this); /*Inicializamos el dialog*/

        /*Creamos la solicitud para iniciar sesión con google*/
        crearSolicitud();

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
        /*Evento al precionar el boton de iniciar con google*/
        ingresargoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
    }

    /*Solicitud para google*/
    private void crearSolicitud() {
        //Configuramos el inicio de sesión de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //Creamos un google sign in con las opciones especificas de googel
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Creamos la pantalla del google
    private void singIn(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Resultado devuelto al iniciar la intencion desde googlesignApi.getsignIntent
        if (requestCode == RC_SIGN_IN ){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //el inicio de sesion fue exitoso, autentiqqeu con firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AutenticacionFirebase(account);//aqui se ejecuta el metodo para logearnos con google
            }catch (ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*Metodo para autenticar con firebase google*/
    private void AutenticacionFirebase(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //si inicia correctamente
                            FirebaseUser user = firebaseAuth.getCurrentUser();//obtenemos al usuario, el cual quiere iniciar sesion

                            /*Si el usuario inicia sesion por primera vez*/
                            if (task.getResult().getAdditionalUserInfo().isNewUser()){



                                String uid = user.getUid();
                                String email = user.getEmail();
                                String name = user.getDisplayName();


                                //aqui pasamos los parametros
                                /*Creamos un hashmap para enviar los datos a firebase*/

                                HashMap<Object,String> DatosUsuario = new HashMap<>();

                                DatosUsuario.put("uid",uid);
                                DatosUsuario.put("email",email);
                                DatosUsuario.put("name",name);
                                DatosUsuario.put("phone","");
                                /*Dejo la imagen vacia*/
                                DatosUsuario.put("image","");

                                /*Iniciamos la instancia a la base de datos*/
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                /*Creamos la base de datos*/
                                DatabaseReference reference = database.getReference("Usuarios_de_la_app");
                                /*El nombre de la base de datos es Ususarios_de_la_app*/
                                reference.child(uid).setValue(DatosUsuario);

                            }
                            //Nos dirija al main activity
                            startActivity(new Intent(Login.this,MainActivity.class));
                        }
                        else{
                            Dialog_No_Inicio();
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
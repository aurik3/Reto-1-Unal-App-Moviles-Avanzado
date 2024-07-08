package com.aurik.unalreto1.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import com.aurik.unalreto1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextCedula;
    private EditText editTextCelular;
    private EditText editTextDireccion;
    private EditText editTextCorreo;
    private EditText editTextContrasenaR;
    private EditText editTextContrasenaConfiR;
    private Button buttonRegistrar;
    private Button buttonIngresar;
    private SwitchCompat switchAdmin;
    private RadioButton terminosYCondiciones;
    private RadioButton trataimentosDeDatos;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Inicializa Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Vincula los componentes de la interfaz de usuario con las variables
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextCedula = findViewById(R.id.editTextCedula);
        editTextCelular = findViewById(R.id.editTextCelular);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasenaR = findViewById(R.id.editTextContrasenaR);
        editTextContrasenaConfiR = findViewById(R.id.editTextContrasenaConfiR);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        terminosYCondiciones = findViewById(R.id.terminosYCondiciones);
        trataimentosDeDatos = findViewById(R.id.trataimentosDeDatos);
        switchAdmin = findViewById(R.id.switchAdmin);

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finaliza esta actividad para que el usuario no pueda volver atrás
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String cedula = editTextCedula.getText().toString().trim();
        String celular = editTextCelular.getText().toString().trim();
        String direccion = editTextDireccion.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contrasenaR = editTextContrasenaR.getText().toString().trim();
        String contrasenaConfiR = editTextContrasenaConfiR.getText().toString().trim();

        boolean isAdmin = switchAdmin.isChecked();

        boolean aceptoTerminos = terminosYCondiciones.isChecked();
        boolean aceptoTratamiento = trataimentosDeDatos.isChecked();

        if(nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || celular.isEmpty() || direccion.isEmpty() || correo.isEmpty() || contrasenaR.isEmpty() || contrasenaConfiR.isEmpty()){
            Toast.makeText(this, "Por favor diligenciar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "El correo no es válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(contrasenaR.length() < 6){
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if(celular.length() < 10){
            Toast.makeText(this, "El celular debe tener al menos 10 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if(!contrasenaR.equals(contrasenaConfiR)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }
        if(!aceptoTerminos){
            Toast.makeText(this, "Por favor acepte los términos y condiciones", Toast.LENGTH_LONG).show();
            return;
        }
        if(!aceptoTratamiento){
            Toast.makeText(this, "Por favor acepte la política de privacidad", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, contrasenaR)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase Auth", "Se ha registrado el usuario correctamente");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("nombre", nombre);
                            userData.put("apellido", apellido);
                            userData.put("cedula", cedula);
                            userData.put("celular", celular);
                            userData.put("direccion", direccion);
                            userData.put("correo", correo);
                            userData.put("contrasena", contrasenaR);
                            userData.put("isAdmin", isAdmin ? "1" : "0");
                            db.collection("usuarios")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Firestore", "Usuario registrado exitosamente en Firestore");
                                            Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            editTextNombre.setText("");
                            editTextApellido.setText("");
                            editTextCedula.setText("");
                            editTextCelular.setText("");
                            editTextDireccion.setText("");
                            editTextCorreo.setText("");
                            editTextContrasenaR.setText("");
                            editTextContrasenaConfiR.setText("");

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Finaliza esta actividad para que el usuario no pueda volver atrás
                        }

                    } else {
                        Log.e("Firebase Authentication", "Error al registrar usuario en Firebase Authentication", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error al registrar usuario en Firebase Authentication", Toast.LENGTH_SHORT).show();

                    }
                }

        );
    }
}
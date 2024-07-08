package com.aurik.unalreto1.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aurik.unalreto1.R;

public class LoginActivity extends AppCompatActivity {

    private Button buttonRegistrar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        buttonRegistrar = findViewById(R.id.buttonRegistrar);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
               startActivity(intent);
               finish(); // Finaliza esta actividad para que el usuario no pueda volver atrás
           }
       });
    }
}
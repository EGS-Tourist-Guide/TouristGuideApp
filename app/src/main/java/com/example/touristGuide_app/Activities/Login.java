package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.touristGuide_app.R;
import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {

    private EditText passwordEditText;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        passwordEditText = findViewById(R.id.password);

        MaterialButton loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPassword = passwordEditText.getText().toString();
                if(userPassword.equals("admin")) {
                    openMainRoom();
                } else {
                    Toast.makeText(Login.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar o clique no ícone do olho
        passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // Método para abrir a próxima página após o login bem-sucedido
    private void openMainRoom() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Método para alternar a visibilidade da senha
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}

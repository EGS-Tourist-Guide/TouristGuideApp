package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.touristGuide_app.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText passwordEditText;
    private EditText mailEditText;
    private boolean isPasswordVisible = false;

    private static final int GOOGLE_LOGIN_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mailEditText = findViewById(R.id.mail);
        passwordEditText = findViewById(R.id.password);

        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = mailEditText.getText().toString();
                
                String userPassword = passwordEditText.getText().toString();
                //sendLoginRequest(userMail, userPassword);
                // String userId = "1";
                // openMainRoom(userId);
                // DESCOMENTAR EM CIMA DEPOIS
                openMainRoom(userMail);
            }
        });

        findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para a página de login do Google
                String googleLoginUrl = "http://192.168.26.112:3000/v1/login";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleLoginUrl));
                startActivityForResult(intent, GOOGLE_LOGIN_REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // Processar os dados retornados da autenticação do Google
            if (data != null) {
                String userId = data.getStringExtra("userId");
                openMainRoom(userId);
            }
        }
    }

    // Método para enviar a solicitação de login
    private void sendLoginRequest(String mail, String password) {
        // Construir o JSON com as credenciais
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("clientName", mail);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar a solicitação POST para o servidor
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.26.112:3000/retrieve-api-key", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Processar a resposta do servidor
                            String apiKey = response.getString("apiKey");
                            // Abrir a próxima página após o login bem-sucedido
                            openMainRoom(apiKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Tratar erros de resposta
                Toast.makeText(Login.this, "Erro no login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Adicionar a solicitação à fila de solicitações
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    // Método para abrir a próxima página após o login bem-sucedido
    private void openMainRoom(String userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        Toast.makeText(this, "Enviou id "+ userId+" para MainActivity", Toast.LENGTH_SHORT).show();
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
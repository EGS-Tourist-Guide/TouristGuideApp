package com.example.touristGuide_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mailEditText = findViewById(R.id.mail);
        passwordEditText = findViewById(R.id.password);

        MaterialButton loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = mailEditText.getText().toString();
                String userPassword = passwordEditText.getText().toString();
                // Versão sem endpoint
                if(userPassword.equals("admin")) {
                    openMainRoom();
                } else {
                    Toast.makeText(Login.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                }
                // Versão com endpoint TROCAR AQUI
                // sendLoginRequest(userMail, userPassword);
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

    // Método para enviar a solicitação de login
    private void sendLoginRequest(String mail, String password) {
        // Construir o JSON com as credenciais
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("clientName", mail); // Aqui você pode ajustar o nome do campo dependendo do que o servidor espera
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar a solicitação POST para o servidor
        // O código para enviar a solicitação e processar a resposta é semelhante ao exemplo anterior
        // Certifique-se de substituir a URL correta e tratar os erros de resposta
        
        // Enviar a solicitação POST para o servidor
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://localhost:3000/retrieve-api-key", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Processar a resposta do servidor
                            String userId = response.getString("userId");
                            // Abrir a próxima página após o login bem-sucedido
                            // TROCAR AQUI
                            // openMainRoom(userId);
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

    // Versao com EndPoint TROCAR AQUI
    // // Método para abrir a próxima página após o login bem-sucedido
    // private void openMainRoom(String userId) {
    //     Intent intent = new Intent(this, MainActivity.class);
    //     intent.putExtra("userId", userId);
    //     startActivity(intent);
    // }
    // Versao sem EndPoint
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

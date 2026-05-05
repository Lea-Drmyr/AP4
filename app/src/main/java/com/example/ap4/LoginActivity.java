package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ap4.model.AuthRequest;
import com.example.ap4.model.AuthReponse;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPswd);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthRequest authRequest = new AuthRequest(email, pass);
        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);

        api.login(authRequest).enqueue(new Callback<AuthReponse>() {
            @Override
            public void onResponse(Call<AuthReponse> call, Response<AuthReponse> reponse) {

                if (reponse.isSuccessful() && reponse.body() != null) {
                    AuthReponse res = reponse.body();

                    // SAUVEGARDE DU TOKEN ET DE L'ID UTILISATEUR
                    TokenManager.saveToken(LoginActivity.this, res.getToken());
                    if (res.getData() != null && res.getData().getUser() != null) {
                        TokenManager.saveUserId(LoginActivity.this, res.getData().getUser().getId());
                    }

                    Log.d("LoginActivity", "Role reçu: " + res.getRole());
                    redirectByUserRole(res.getRole());
                    return;
                }

                handleApiError(reponse);
            }

            @Override
            public void onFailure(Call<AuthReponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void handleApiError(Response<AuthReponse> response) {
        try {
            String error = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
            Toast.makeText(this, "Erreur " + response.code(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectByUserRole(String role) {
        Intent intent;
        if (role != null && role.equalsIgnoreCase("Admin")) {
            intent = new Intent(LoginActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, UserActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ap4.model.AuthReponse;
import com.example.ap4.model.UserUpdate;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etMail, etDateNaissance, etNumTel, etAdresse;
    private Button btnUpdate, btnDelete, btnLogout;
    private ApiService apiService;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        apiService = RetrofitClient.getInstance(this).create(ApiService.class);
        
        currentUserId = TokenManager.getUserId(this);
        
        if (currentUserId != -1) {
            loadUserProfile();
        } else {
            Toast.makeText(this, "Session expirée", Toast.LENGTH_SHORT).show();
            logout();
        }
        
        setupListeners();
    }

    private void initViews() {
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etMail = findViewById(R.id.etMail);
        etDateNaissance = findViewById(R.id.etDateNaissance);
        etNumTel = findViewById(R.id.etNumTel);
        etAdresse = findViewById(R.id.etAdresse);
        
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnUpdate.setOnClickListener(v -> updateProfile());
        btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserProfile() {
        apiService.getCurrentUser(currentUserId).enqueue(new Callback<AuthReponse.User>() {
            @Override
            public void onResponse(Call<AuthReponse.User> call, Response<AuthReponse.User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthReponse.User user = response.body();
                    etNom.setText(user.getUsername());
                    // Remplir les autres champs si disponibles dans AuthReponse.User
                }
            }

            @Override
            public void onFailure(Call<AuthReponse.User> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile() {
        UserUpdate update = new UserUpdate();
        update.setIdAdherents(String.valueOf(currentUserId));
        update.setNom(etNom.getText().toString().trim());
        update.setPrenom(etPrenom.getText().toString().trim());
        update.setMail(etMail.getText().toString().trim());
        update.setDateNaissance(etDateNaissance.getText().toString().trim());
        update.setNumTel(etNumTel.getText().toString().trim());
        update.setAdresse(etAdresse.getText().toString().trim());

        apiService.updateProfile(currentUserId, update).enqueue(new Callback<AuthReponse.User>() {
            @Override
            public void onResponse(Call<AuthReponse.User> call, Response<AuthReponse.User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Profil mis à jour !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthReponse.User> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Échec réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le compte")
                .setMessage("Voulez-vous vraiment supprimer votre compte ?")
                .setPositiveButton("Oui, supprimer", (dialog, which) -> deleteAccount())
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteAccount() {
        apiService.deleteAdherent(currentUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Compte supprimé", Toast.LENGTH_SHORT).show();
                    logout();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Erreur de suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        TokenManager.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

package com.example.ap4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ap4.model.AdherentModel;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAdherentActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etMail, etDateNaissance, etNumTel, etAdresse, etPassword, etRole, etAbonnement;
    private Button btnUpdate;
    private AdherentModel adherent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_adherent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        // Récupérer l'adhérent passé via l'Intent
        adherent = (AdherentModel) getIntent().getSerializableExtra("ADHERENT_DATA");

        if (adherent != null) {
            fillFields();
        } else {
            Toast.makeText(this, "Erreur : Données de l'adhérent manquantes", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdate.setOnClickListener(v -> updateAdherent());
    }

    private void initViews() {
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etMail = findViewById(R.id.etMail);
        etDateNaissance = findViewById(R.id.etDateNaissance);
        etNumTel = findViewById(R.id.etNumTel);
        etAdresse = findViewById(R.id.etAdresse);
        etPassword = findViewById(R.id.etPassword);
        etRole = findViewById(R.id.etRole);
        etAbonnement = findViewById(R.id.etAbonnement);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void fillFields() {
        etNom.setText(adherent.getNom());
        etPrenom.setText(adherent.getPrenom());
        etMail.setText(adherent.getMail());
        etDateNaissance.setText(adherent.getDateNaissance());
        etNumTel.setText(adherent.getNumTel());
        etAdresse.setText(adherent.getAdresse());
        etRole.setText(adherent.getRole());
        etAbonnement.setText(adherent.getIdAbonnement());
        // On ne remplit pas le mot de passe pour des raisons de sécurité
    }

    private void updateAdherent() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String mail = etMail.getText().toString().trim();
        String dateN = etDateNaissance.getText().toString().trim();
        String tel = etNumTel.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = etRole.getText().toString().trim();
        String abonnement = etAbonnement.getText().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mettre à jour l'objet
        adherent.setNom(nom);
        adherent.setPrenom(prenom);
        adherent.setMail(mail);
        adherent.setDateNaissance(dateN);
        adherent.setNumTel(tel);
        adherent.setAdresse(adresse);
        adherent.setRole(role);
        adherent.setIdAbonnement(abonnement);
        
        // On ne change le mot de passe que s'il est saisi
        if (!password.isEmpty()) {
            adherent.setMotDePasse(password);
        }

        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);
        api.updateAdherent(adherent.getId_Adherent(), adherent).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateAdherentActivity.this, "Adhérent mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
                        Log.e("UPDATE_ERROR", error);
                        Toast.makeText(UpdateAdherentActivity.this, "Erreur serveur : " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateAdherentActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

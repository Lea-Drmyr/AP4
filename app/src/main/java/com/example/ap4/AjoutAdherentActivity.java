package com.example.ap4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ap4.model.AdherentModel;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjoutAdherentActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etMail, etDateNaissance, etNumTel, etAdresse, etPassword, etRole, etAbonnement;
    private Button btnSave;
    private TextView tvAjoutTitle;
    private AdherentModel adherentAModifier;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_adherent);

        initViews();

        // Vérifier si on est en mode modification
        if (getIntent().hasExtra("ADHERENT_DATA")) {
            adherentAModifier = (AdherentModel) getIntent().getSerializableExtra("ADHERENT_DATA");
            isEditMode = true;
            setupEditMode();
        }

        btnSave.setOnClickListener(v -> saveAdherent());
    }

    private void initViews() {
        tvAjoutTitle = findViewById(R.id.tvAjoutTitle);
        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etMail = findViewById(R.id.etMail);
        etDateNaissance = findViewById(R.id.etDateNaissance);
        etNumTel = findViewById(R.id.etNumTel);
        etAdresse = findViewById(R.id.etAdresse);
        etPassword = findViewById(R.id.etPassword);
        etRole = findViewById(R.id.etRole);
        etAbonnement = findViewById(R.id.etAbonnement);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupEditMode() {
        tvAjoutTitle.setText("Modifier l'Adhérent");
        btnSave.setText("Enregistrer les modifications");

        if (adherentAModifier != null) {
            etNom.setText(adherentAModifier.getNom());
            etPrenom.setText(adherentAModifier.getPrenom());
            etMail.setText(adherentAModifier.getMail());
            etDateNaissance.setText(adherentAModifier.getDateNaissance());
            etNumTel.setText(adherentAModifier.getNumTel());
            etAdresse.setText(adherentAModifier.getAdresse());
            etPassword.setText(adherentAModifier.getMotDePasse());
            etRole.setText(adherentAModifier.getRole());
            etAbonnement.setText(adherentAModifier.getIdAbonnement());
        }
    }

    private void saveAdherent() {
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String mailStr = etMail.getText().toString().trim();
        String dateN = etDateNaissance.getText().toString().trim();
        String tel = etNumTel.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = etRole.getText().toString().trim();
        String abonnement = etAbonnement.getText().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || mailStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        AdherentModel adherent = isEditMode ? adherentAModifier : new AdherentModel();
        adherent.setNom(nom);
        adherent.setPrenom(prenom);
        adherent.setMail(mailStr);
        adherent.setDateNaissance(dateN);
        adherent.setNumTel(tel);
        adherent.setAdresse(adresse);
        adherent.setMotDePasse(password);
        adherent.setRole(role);
        adherent.setIdAbonnement(abonnement);

        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);
        Call<Void> call;

        if (isEditMode) {
            call = api.updateAdherent(adherent.getId_Adherent(), adherent);
        } else {
            call = api.addAdherent(adherent);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String msg = isEditMode ? "Modifié avec succès" : "Ajouté avec succès";
                    Toast.makeText(AjoutAdherentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
                        Log.e("API_ERROR", "Code: " + response.code() + " - Body: " + errorBody);
                        Toast.makeText(AjoutAdherentActivity.this, "Erreur serveur : " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AjoutAdherentActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

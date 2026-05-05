package com.example.ap4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ap4.model.AdherentModel;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjoutAdherentActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etMail, etDateNaissance, etNumTel, etAdresse, etPassword, etRole, etAbonnement;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_adherent);

        initViews();

        btnSave.setOnClickListener(v -> saveAdherent());
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
        btnSave = findViewById(R.id.btnSave);
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

        AdherentModel adherent = new AdherentModel();
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
        api.addAdherent(adherent).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AjoutAdherentActivity.this, "Adhérent ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AjoutAdherentActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AjoutAdherentActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

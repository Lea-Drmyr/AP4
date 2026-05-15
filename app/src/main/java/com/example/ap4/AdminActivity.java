package com.example.ap4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap4.adapter.AdherentRecyclerAdapter;
import com.example.ap4.model.AdherentModel;
import com.example.ap4.network.ApiService;
import com.example.ap4.network.RetrofitClient;
import com.example.ap4.session.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private AdherentRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private final List<AdherentModel> adherentList = new ArrayList<>();
    private AdherentModel selectedAdherent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerUsers);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAddAdherent = findViewById(R.id.btnAddAdherent);

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdherentRecyclerAdapter(this, adherentList);
        recyclerView.setAdapter(adapter);

        // Gestion des actions sur les items (Modifier / Supprimer)
        adapter.setOnItemActionListener(new AdherentRecyclerAdapter.OnItemActionListener() {
            @Override
            public void onModify(AdherentModel adherent) {
                // On passe l'objet adherent à l'activité de modification
                Intent intent = new Intent(AdminActivity.this, AjoutAdherentActivity.class);
                intent.putExtra("ADHERENT_DATA", adherent);
                startActivity(intent);
            }

            @Override
            public void onDelete(AdherentModel adherent) {
                selectedAdherent = adherent;
                confirmDeletion();
            }
        });

        btnAddAdherent.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AjoutAdherentActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            TokenManager.clear(this);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void confirmDeletion() {
        if (selectedAdherent == null) return;
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'adhérent")
                .setMessage("Voulez-vous vraiment supprimer " + selectedAdherent.getNom() + " " + selectedAdherent.getPrenom() + " ?")
                .setPositiveButton("Oui, supprimer", (dialog, which) -> deleteUser())
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void deleteUser() {
        if (selectedAdherent == null) return;

        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);
        api.deleteAdherent(selectedAdherent.getId_Adherent()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Adhérent supprimé", Toast.LENGTH_SHORT).show();
                    loadUsers(); // Recharger la liste
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erreur inconnue";
                        Log.e("API_ERROR", "Code: " + response.code() + " - Body: " + errorBody);
                        Toast.makeText(AdminActivity.this, "Erreur serveur (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Échec de connexion au serveur", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getInstance(this).create(ApiService.class);
        api.getAllAdherent().enqueue(new Callback<List<AdherentModel>>() {
            @Override
            public void onResponse(Call<List<AdherentModel>> call, Response<List<AdherentModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adherentList.clear();
                    adherentList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminActivity.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdherentModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

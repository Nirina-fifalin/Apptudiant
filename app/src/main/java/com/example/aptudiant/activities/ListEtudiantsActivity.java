package com.example.aptudiant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aptudiant.R;
import com.example.aptudiant.adapters.EtudiantAdapter;
import com.example.aptudiant.api.ApiClient;
import com.example.aptudiant.api.ApiInterface;
import com.example.aptudiant.models.Etudiant;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEtudiantsActivity extends AppCompatActivity implements EtudiantAdapter.OnItemClickListener {

    private EtudiantAdapter adapter;
    private final List<Etudiant> etudiantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EtudiantAdapter(etudiantList, this);
        recyclerView.setAdapter(adapter);

        loadEtudiants();
    }

    private void loadEtudiants() {
        ApiInterface apiService = ApiClient.getApiService();
        apiService.getAllEtudiants().enqueue(new Callback<List<Etudiant>>() {
            @Override
            public void onResponse(@NonNull Call<List<Etudiant>> call, @NonNull Response<List<Etudiant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    etudiantList.clear();
                    etudiantList.addAll(response.body());
                    adapter.notifyItemRangeChanged(0, etudiantList.size()); // plus précis que notifyDataSetChanged
                } else {
                    showError("Erreur: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Etudiant>> call, @NonNull Throwable t) {
                showError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Etudiant etudiant) {
        Intent intent = new Intent(this, AddEditEtudiantActivity.class);
        intent.putExtra("ETUDIANT", etudiant);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Etudiant etudiant, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Voulez-vous vraiment supprimer cet étudiant ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteEtudiant(etudiant.getNumEt(), position))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteEtudiant(String numEt, int position) {
        ApiInterface apiService = ApiClient.getApiService();
        apiService.deleteEtudiant(numEt).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    etudiantList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(ListEtudiantsActivity.this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                } else {
                    showError("Échec de la suppression : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                showError("Erreur de suppression : " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEtudiants();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

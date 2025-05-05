package com.example.aptudiant.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aptudiant.R;
import com.example.aptudiant.api.ApiClient;
import com.example.aptudiant.api.ApiInterface;
import com.example.aptudiant.models.Etudiant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditEtudiantActivity extends AppCompatActivity {

    private TextInputEditText etNumEt, etNom, etMoyenne;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initViews();
        checkEditMode();
        setupSaveButton();
    }

    private void initViews() {
        etNumEt = findViewById(R.id.etNumEt);
        etNom = findViewById(R.id.etNom);
        etMoyenne = findViewById(R.id.etMoyenne);
    }

    private void checkEditMode() {
        Etudiant existingEtudiant = (Etudiant) getIntent().getSerializableExtra("ETUDIANT");
        if (existingEtudiant != null) {
            isEditMode = true;
            populateFields(existingEtudiant);
        }
    }

    private void populateFields(@NonNull Etudiant etudiant) {
        etNumEt.setText(etudiant.getNumEt());
        etNom.setText(etudiant.getNom());
        etMoyenne.setText(String.valueOf(etudiant.getMoyenne()));
        etNumEt.setEnabled(false);
    }

    private void setupSaveButton() {
        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                Etudiant etudiant = createEtudiantFromInput();
                if (etudiant != null) {
                    if (isEditMode) {
                        updateEtudiant(etudiant);
                    } else {
                        createEtudiant(etudiant);
                    }
                }
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etNumEt.getText())) {
            etNumEt.setError("Numéro étudiant requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(etNom.getText())) {
            etNom.setError("Nom requis");
            isValid = false;
        }

        try {
            String moyenneText = etMoyenne.getText() != null ? etMoyenne.getText().toString() : "";
            if (TextUtils.isEmpty(moyenneText)) {
                etMoyenne.setError("Moyenne requise");
                isValid = false;
            } else {
                double moyenne = Double.parseDouble(moyenneText);
                if (moyenne < 0 || moyenne > 20) {
                    etMoyenne.setError("Moyenne entre 0 et 20");
                    isValid = false;
                }
            }
        } catch (NumberFormatException e) {
            etMoyenne.setError("Valeur numérique requise");
            isValid = false;
        }

        return isValid;
    }

    private Etudiant createEtudiantFromInput() {
        try {
            String numEt = etNumEt.getText() != null ? etNumEt.getText().toString().trim() : "";
            String nom = etNom.getText() != null ? etNom.getText().toString().trim() : "";
            String moyenneText = etMoyenne.getText() != null ? etMoyenne.getText().toString() : "0";

            return new Etudiant(numEt, nom, Double.parseDouble(moyenneText));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Erreur de format des données", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void createEtudiant(@NonNull Etudiant etudiant) {
        ApiInterface apiService = ApiClient.getApiService();
        Call<Etudiant> call = apiService.createEtudiant(etudiant);

        call.enqueue(new Callback<Etudiant>() {
            @Override
            public void onResponse(@NotNull Call<Etudiant> call, @NotNull Response<Etudiant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showSuccess("Étudiant créé");
                } else {
                    showError("Erreur: " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Etudiant> call, @NotNull Throwable t) {
                showError("Erreur réseau");
            }
        });
    }

    private void updateEtudiant(@NonNull Etudiant etudiant) {
        ApiInterface apiService = ApiClient.getApiService();
        Call<Etudiant> call = apiService.updateEtudiant(etudiant.getNumEt(), etudiant);

        call.enqueue(new Callback<Etudiant>() {
            @Override
            public void onResponse(@NotNull Call<Etudiant> call, @NotNull Response<Etudiant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showSuccess("Étudiant mis à jour");
                } else {
                    showError("Erreur: " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Etudiant> call, @NotNull Throwable t) {
                showError("Erreur réseau");
            }
        });
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
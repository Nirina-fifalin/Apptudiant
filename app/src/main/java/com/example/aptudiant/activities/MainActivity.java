package com.example.aptudiant.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aptudiant.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton btnList = findViewById(R.id.btnList);
        MaterialButton btnAdd = findViewById(R.id.btnAdd);
        MaterialButton btnStats = findViewById(R.id.btnStats);

        btnList.setOnClickListener(v -> startActivity(new Intent(this, ListEtudiantsActivity.class)));
        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddEditEtudiantActivity.class)));
        btnStats.setOnClickListener(v -> startActivity(new Intent(this, StatsActivity.class)));
    }
}

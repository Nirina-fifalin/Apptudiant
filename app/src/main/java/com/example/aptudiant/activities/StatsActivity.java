package com.example.aptudiant.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aptudiant.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private MaterialTextView tvMoyenne, tvMin, tvMax;
    private BarChart barChart;
    private PieChart pieChart;
    private static final String TAG = "StatsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        tvMoyenne = findViewById(R.id.tvMoyenne);
        tvMin = findViewById(R.id.tvMin);
        tvMax = findViewById(R.id.tvMax);
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        ImageButton btnSwitchChart = findViewById(R.id.btnSwitchChart);

        barChart.setVisibility(BarChart.VISIBLE);
        pieChart.setVisibility(PieChart.GONE);

        final boolean[] isPieVisible = {false};

        btnSwitchChart.setOnClickListener(v -> {
            isPieVisible[0] = !isPieVisible[0];
            barChart.setVisibility(isPieVisible[0] ? BarChart.GONE : BarChart.VISIBLE);
            pieChart.setVisibility(isPieVisible[0] ? PieChart.VISIBLE : PieChart.GONE);
        });

        getStats();
    }

    private void getStats() {
        String url = "http://10.0.2.2:8080/api/etudiants/stats";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        double moyenne = response.getDouble("moyenne");
                        double min = response.getDouble("min");
                        double max = response.getDouble("max");

                        tvMoyenne.setText(getString(R.string.stats_moyenne, moyenne));
                        tvMin.setText(getString(R.string.stats_min, min));
                        tvMax.setText(getString(R.string.stats_max, max));

                        afficherBarChart(moyenne, min, max);
                        afficherPieChart(moyenne, min, max);

                    } catch (JSONException e) {
                        Log.e(TAG, "Erreur parsing JSON", e);
                    }
                },
                error -> Log.e(TAG, "Erreur requête API : " + error.getMessage())
        );

        queue.add(request);
    }

    private void afficherBarChart(double moyenne, double min, double max) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) moyenne));
        entries.add(new BarEntry(1f, (float) min));
        entries.add(new BarEntry(2f, (float) max));

        BarDataSet dataSet = new BarDataSet(entries, "Statistiques");
        dataSet.setColors(
                ContextCompat.getColor(this, R.color.teal_200),
                ContextCompat.getColor(this, R.color.purple_200),
                ContextCompat.getColor(this, R.color.purple_700)
        );
        dataSet.setValueTextSize(14f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Moyenne", "Min", "Max"}));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setText("Statistiques générales");
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private void afficherPieChart(double moyenne, double min, double max) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) moyenne, "Moyenne"));
        entries.add(new PieEntry((float) min, "Min"));
        entries.add(new PieEntry((float) max, "Max"));

        PieDataSet dataSet = new PieDataSet(entries, "Répartition");
        dataSet.setColors(
                ContextCompat.getColor(this, R.color.teal_200),
                ContextCompat.getColor(this, R.color.purple_200),
                ContextCompat.getColor(this, R.color.purple_700)
        );
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setText("Répartition des statistiques");
        pieChart.setEntryLabelTextSize(12f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}

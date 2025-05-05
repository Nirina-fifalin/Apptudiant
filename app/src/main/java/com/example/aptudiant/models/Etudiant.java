package com.example.aptudiant.models;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class Etudiant implements Serializable {
    private String numEt;
    private String nom;
    private double moyenne;

    public Etudiant() {}

    public Etudiant(String numEt, String nom, double moyenne) {
        this.numEt = numEt;
        this.nom = nom;
        this.moyenne = moyenne;
    }

    public String getNumEt() { return numEt; }
    public void setNumEt(String numEt) { this.numEt = numEt; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }

    public String getObservation() {
        if (moyenne < 5) return "Exclus";
        if (moyenne < 10) return "Redoublant";
        return "Admis";
    }

    @NonNull
    @Override
    public String toString() {
        return nom + " (" + numEt + ")";
    }
}
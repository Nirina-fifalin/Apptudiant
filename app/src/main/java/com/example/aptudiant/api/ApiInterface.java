package com.example.aptudiant.api;

import com.example.aptudiant.models.Etudiant;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("etudiants")
    Call<List<Etudiant>> getAllEtudiants();

    @POST("etudiants")
    Call<Etudiant> createEtudiant(@Body Etudiant etudiant);

    @PUT("etudiants/{numEt}")
    Call<Etudiant> updateEtudiant(@Path("numEt") String numEt, @Body Etudiant etudiant);

    @DELETE("etudiants/{numEt}")
    Call<Void> deleteEtudiant(@Path("numEt") String numEt);

    @GET("etudiants/stats")
    Call<Map<String, Double>> getStats();
}

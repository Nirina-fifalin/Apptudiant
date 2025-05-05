package com.example.aptudiant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aptudiant.R;
import com.example.aptudiant.models.Etudiant;

import java.util.List;
import java.util.Locale;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private final List<Etudiant> etudiantList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Etudiant etudiant);
        void onItemLongClick(Etudiant etudiant, int position);
    }

    public EtudiantAdapter(List<Etudiant> etudiantList, OnItemClickListener listener) {
        this.etudiantList = etudiantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_etudiant, parent, false);
        return new EtudiantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiantList.get(position);
        holder.bind(etudiant, listener, position);
    }

    @Override
    public int getItemCount() {
        return etudiantList.size();
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNumEt;
        private final TextView tvNom;
        private final TextView tvMoyenne;
        private final TextView tvObservation;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumEt = itemView.findViewById(R.id.tvNumEt);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvMoyenne = itemView.findViewById(R.id.tvMoyenne);
            tvObservation = itemView.findViewById(R.id.tvObservation);
        }

        public void bind(final Etudiant etudiant, final OnItemClickListener listener, final int position) {
            tvNumEt.setText(etudiant.getNumEt());
            tvNom.setText(etudiant.getNom());
            tvMoyenne.setText(String.format(Locale.getDefault(), "Moyenne: %.2f", etudiant.getMoyenne()));
            tvObservation.setText(etudiant.getObservation());

            itemView.setOnClickListener(v -> listener.onItemClick(etudiant));
            itemView.setOnLongClickListener(v -> {
                listener.onItemLongClick(etudiant, position);
                return true;
            });
        }
    }
}

package com.example.ap4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ap4.R;
import com.example.ap4.model.AdherentModel;

import java.util.List;

public class AdherentRecyclerAdapter extends RecyclerView.Adapter<AdherentRecyclerAdapter.ViewHolder> {

    private final List<AdherentModel> adherents;
    private final Context context;

    // Nouvelle interface pour gérer Modifier + Supprimer
    public interface OnItemActionListener {
        void onModify(AdherentModel adherent);
        void onDelete(AdherentModel adherent);
    }

    private OnItemActionListener listener;

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public AdherentRecyclerAdapter(Context context, List<AdherentModel> adherents) {
        this.context = context;
        this.adherents = adherents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_adherent, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdherentModel adherent = adherents.get(position);

        holder.txtName.setText(adherent.getNom() + " " + adherent.getPrenom());
        holder.txtRole.setText(adherent.getRole());
        holder.txtEmail.setText(adherent.getMail());
        holder.txtPhone.setText(adherent.getNumTel());

        // Bouton Modifier
        holder.btnModifier.setOnClickListener(v -> {
            if (listener != null) listener.onModify(adherent);
        });

        // Bouton Supprimer
        holder.btnSupprimer.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(adherent);
        });
    }

    @Override
    public int getItemCount() {
        return adherents != null ? adherents.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtRole, txtEmail, txtPhone;
        Button btnModifier, btnSupprimer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.itemTxtName);
            txtRole = itemView.findViewById(R.id.itemTxtRole);
            txtEmail = itemView.findViewById(R.id.itemTxtEmail);
            txtPhone = itemView.findViewById(R.id.itemTxtPhone);

            btnModifier = itemView.findViewById(R.id.btnModifier);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
        }
    }
}
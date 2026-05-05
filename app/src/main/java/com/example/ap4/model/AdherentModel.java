package com.example.ap4.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AdherentModel implements Serializable {
    @SerializedName("idAdherents")
    private int idAdherents;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("mail")
    private String mail;

    @SerializedName("dateNaissance")
    private String dateNaissance;

    @SerializedName("numTel")
    private String numTel;

    @SerializedName("adresse")
    private String adresse;

    @SerializedName("MotDePasse")
    private String MotDePasse;

    @SerializedName("Role")
    private String role;

    @SerializedName("idAbonnement")
    private String idAbonnement;

    public AdherentModel() {}

    public int getId_Adherent() { return idAdherents; }
    public void setId_Adherent(int id_Adherent) { this.idAdherents = id_Adherent; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getNumTel() { return numTel; }
    public void setNumTel(String numTel) { this.numTel = numTel; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getMotDePasse() { return MotDePasse; }
    public void setMotDePasse(String MotDePasse) { this.MotDePasse = MotDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(String idAbonnement) { this.idAbonnement = idAbonnement; }
}

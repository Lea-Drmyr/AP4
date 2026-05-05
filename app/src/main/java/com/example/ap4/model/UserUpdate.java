package com.example.ap4.model;

import com.google.gson.annotations.SerializedName;

public class UserUpdate {
    @SerializedName("idAdherents")
    private String idAdherents;
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
    @SerializedName("MotDePasse")
    private String adresse;
    @SerializedName("idAbonnement")
    private int idAbonnement;
    @SerializedName("role")
    private String role;

    public UserUpdate() {}

    public UserUpdate(String idAdherents, String nom, String prenom, String mail, String dateNaissance, String numTel, String adresse, int idAbonnement, String role) {
        this.idAdherents = idAdherents;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.dateNaissance = dateNaissance;
        this.numTel = numTel;
        this.adresse = adresse;
        this.idAbonnement = idAbonnement;
        this.role = role;
    }

    public String getIdAdherents() { return idAdherents; }
    public void setIdAdherents(String idAdherents) { this.idAdherents = idAdherents; }

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

    public int getIdAbonnement() { return idAbonnement; }
    public void setIdAbonnement(int idAbonnement) { this.idAbonnement = idAbonnement; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

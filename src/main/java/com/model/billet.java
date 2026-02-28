package model;

import com.compagnieaerienne.dao.*;
import enumeration.classe_billet;
import enumeration.statut_billet;

import java.sql.*;
import java.sql.Date;

public class billet {
    private int numBillet;
    private int idPassager;
    private classe_billet classe;
    private double tarif;
    private statut_billet statut;
    private Date dateEmission;
    private String numVol;
    private String numSiege;

    // 🔹 Constructeur par défaut
    public billet() {}

    // 🔹 Constructeur paramétrique
    public billet(int numBillet, int idPassager, classe_billet classe, double tarif,
                  statut_billet statut, Date dateEmission, String numVol, String numSiege) {
        this.numBillet = numBillet;
        this.idPassager = idPassager;
        this.classe = classe;
        this.tarif = tarif;
        this.statut = statut;
        this.dateEmission = dateEmission;
        this.numVol = numVol;
        this.numSiege = numSiege;
    }

    // 🔹 Getters & Setters
    public int getNumBillet() { return numBillet; }
    public void setNumBillet(int string) { this.numBillet = string; }

    public int getIdPassager() { return idPassager; }
    public void setIdPassager(int idPassager) { this.idPassager = idPassager; }

    public classe_billet getClasse() { return classe; }
    public void setClasse(classe_billet classe) { this.classe = classe; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public statut_billet getStatut() { return statut; }
    public void setStatut(statut_billet statut) { this.statut = statut; }

    public Date getDateEmission() { return dateEmission; }
    public void setDateEmission(Date dateEmission) { this.dateEmission = dateEmission; }

    public String getNumVol() { return numVol; }
    public void setNumVol(String numVol) { this.numVol = numVol; }

    public String getNumSiege() { return numSiege; }
    public void setNumSiege(String numSiege) { this.numSiege = numSiege; }

    // ===================== CRUD =======================

    // 🔍 Chercher billet par numéro
    public static billet chercher(int numBillet) {
        billet b = null;
        try {
            Connection con = ConnexionBD.getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Billet WHERE numBillet=?");
            pst.setInt(1, numBillet);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                b = new billet(
                    rs.getInt("numBillet"),
                    rs.getInt("idPassager"),
                    classe_billet.valueOf(rs.getString("classe")),
                    rs.getDouble("tarif"),
                    statut_billet.valueOf(rs.getString("statut")),
                    rs.getDate("dateEmission"),
                    rs.getString("numVol"),
                    rs.getString("numSiege")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur recherche billet : " + e.getMessage());
        }
        return b;
    }

    // ➕ Ajouter
    public void ajouter() {
        try {
            Connection con = ConnexionBD.getConnection();
            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO Billet(idPassager, classe, tarif, statut, dateEmission, numVol, numSiege) VALUES (?,?,?,?,?,?,?)"
            );
            pst.setInt(1, this.idPassager);
            pst.setString(2, this.classe.toString());
            pst.setDouble(3, this.tarif);
            pst.setString(4, this.statut.toString());
            pst.setDate(5, this.dateEmission);
            pst.setString(6, this.numVol);
            pst.setString(7, this.numSiege);

            pst.executeUpdate();
            System.out.println("✔ Billet ajouté !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout billet : " + e.getMessage());
        }
    }

    // ✏️ Modifier
    public void modifier() {
        try {
            Connection con = ConnexionBD.getConnection();
            PreparedStatement pst = con.prepareStatement(
                "UPDATE Billet SET idPassager=?, classe=?, tarif=?, statut=?, dateEmission=?, numVol=?, numSiege=? WHERE numBillet=?"
            );
            pst.setInt(1, this.idPassager);
            pst.setString(2, this.classe.toString());
            pst.setDouble(3, this.tarif);
            pst.setString(4, this.statut.toString());
            pst.setDate(5, this.dateEmission);
            pst.setString(6, this.numVol);
            pst.setString(7, this.numSiege);
            pst.setInt(8, this.numBillet);

            pst.executeUpdate();
            System.out.println("✔ Billet modifié !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification billet : " + e.getMessage());
        }
    }

    // 🗑️ Supprimer
    public static void supprimer(int numBillet) {
        try {
            Connection con = ConnexionBD.getConnection();
            PreparedStatement pst = con.prepareStatement("DELETE FROM Billet WHERE numBillet=?");
            pst.setInt(1, numBillet);
            pst.executeUpdate();
            System.out.println("✔ Billet supprimé !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression billet : " + e.getMessage());
        }
    }
}


package com.compagnieaerienne.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassagerDAO {
    
    // Méthode pour ajouter un passager
    public boolean ajouterPassager(String nom, String prenom, Date dateNaissance, 
                                   String passeport, String nationalite, String typePassager,
                                   String genre, String email, String telephone) {
        String sql = "INSERT INTO passager (nom, prenom, date_naissance, passeport, " +
                     "nationalite, type_passager, genre, email, telephone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setDate(3, dateNaissance);
            pst.setString(4, passeport);
            pst.setString(5, nationalite);
            pst.setString(6, typePassager);
            pst.setString(7, genre);
            pst.setString(8, email);
            pst.setString(9, telephone);
            
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du passager: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour récupérer tous les passagers
    public List<Object[]> getTousLesPassagers() {
        List<Object[]> passagers = new ArrayList<>();
        String sql = "SELECT id_passager, nom, prenom, date_naissance, passeport, " +
                     "nationalite, type_passager, genre, email, telephone " +
                     "FROM passager ORDER BY id_passager";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Object[] passager = new Object[10];
                passager[0] = rs.getInt("id_passager");
                passager[1] = rs.getString("nom");
                passager[2] = rs.getString("prenom");
                passager[3] = rs.getDate("date_naissance");
                passager[4] = rs.getString("passeport");
                passager[5] = rs.getString("nationalite");
                passager[6] = rs.getString("type_passager");
                passager[7] = rs.getString("genre");
                passager[8] = rs.getString("email");
                passager[9] = rs.getString("telephone");
                passagers.add(passager);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des passagers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return passagers;
    }
    
    // Méthode pour modifier un passager
    public boolean modifierPassager(int id, String nom, String prenom, Date dateNaissance,
                                    String passeport, String nationalite, String typePassager,
                                    String genre, String email, String telephone) {
        String sql = "UPDATE passager SET nom=?, prenom=?, date_naissance=?, passeport=?, " +
                     "nationalite=?, type_passager=?, genre=?, email=?, telephone=? " +
                     "WHERE id_passager=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setDate(3, dateNaissance);
            pst.setString(4, passeport);
            pst.setString(5, nationalite);
            pst.setString(6, typePassager);
            pst.setString(7, genre);
            pst.setString(8, email);
            pst.setString(9, telephone);
            pst.setInt(10, id);
            
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du passager: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour supprimer un passager
    public boolean supprimerPassager(int id) {
        String sql = "DELETE FROM passager WHERE id_passager=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du passager: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour rechercher des passagers
    public List<Object[]> rechercherPassagers(String recherche) {
        List<Object[]> passagers = new ArrayList<>();
        String sql = "SELECT id_passager, nom, prenom, date_naissance, passeport, " +
                     "nationalite, type_passager, genre, email, telephone FROM passager " +
                     "WHERE LOWER(nom) LIKE LOWER(?) OR LOWER(prenom) LIKE LOWER(?) " +
                     "OR LOWER(passeport) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) " +
                     "ORDER BY id_passager";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            String pattern = "%" + recherche + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);
            pst.setString(3, pattern);
            pst.setString(4, pattern);
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] passager = new Object[10];
                passager[0] = rs.getInt("id_passager");
                passager[1] = rs.getString("nom");
                passager[2] = rs.getString("prenom");
                passager[3] = rs.getDate("date_naissance");
                passager[4] = rs.getString("passeport");
                passager[5] = rs.getString("nationalite");
                passager[6] = rs.getString("type_passager");
                passager[7] = rs.getString("genre");
                passager[8] = rs.getString("email");
                passager[9] = rs.getString("telephone");
                passagers.add(passager);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
        
        return passagers;
    }
    
    // Méthode pour obtenir un passager par ID
    public Object[] getPassagerParId(int id) {
        String sql = "SELECT * FROM passager WHERE id_passager=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Object[] passager = new Object[10];
                passager[0] = rs.getInt("id_passager");
                passager[1] = rs.getString("nom");
                passager[2] = rs.getString("prenom");
                passager[3] = rs.getDate("date_naissance");
                passager[4] = rs.getString("passeport");
                passager[5] = rs.getString("nationalite");
                passager[6] = rs.getString("type_passager");
                passager[7] = rs.getString("genre");
                passager[8] = rs.getString("email");
                passager[9] = rs.getString("telephone");
                return passager;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du passager: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}

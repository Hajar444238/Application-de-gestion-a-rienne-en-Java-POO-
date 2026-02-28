package com.compagnieaerienne.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BilletDAO {
    
    // Méthode pour ajouter un billet
    public boolean ajouterBillet(int idPassager, String classe, double tarif, 
                                 String statut, Date dateEmission, String numVol, 
                                 String numSiege) {
        String sql = "INSERT INTO billet (id_passager, classe, tarif, statut, " +
                     "date_emission, num_vol, num_siege) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idPassager);
            pst.setString(2, classe);
            pst.setDouble(3, tarif);
            pst.setString(4, statut);
            pst.setDate(5, dateEmission);
            pst.setString(6, numVol);
            pst.setString(7, numSiege);
            
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du billet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour récupérer tous les billets
    public List<Object[]> getTousLesBillets() {
        List<Object[]> billets = new ArrayList<>();
        String sql = "SELECT id_billet, id_passager, classe, tarif, statut, num_vol, num_siege " +
                     "FROM billet ORDER BY id_billet";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                Object[] billet = new Object[7];
                billet[0] = rs.getInt("id_billet");
                billet[1] = rs.getInt("id_passager");
                billet[2] = rs.getString("classe");
                billet[3] = rs.getDouble("tarif") + " DH";
                billet[4] = rs.getString("statut");
                billet[5] = rs.getString("num_vol");
                billet[6] = rs.getString("num_siege");
                billets.add(billet);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des billets: " + e.getMessage());
            e.printStackTrace();
        }
        
        return billets;
    }
    
    // Méthode pour modifier un billet
    public boolean modifierBillet(int idBillet, int idPassager, String classe, 
                                  double tarif, String statut, Date dateEmission,
                                  String numVol, String numSiege) {
        String sql = "UPDATE billet SET id_passager=?, classe=?, tarif=?, statut=?, " +
                     "date_emission=?, num_vol=?, num_siege=? WHERE id_billet=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idPassager);
            pst.setString(2, classe);
            pst.setDouble(3, tarif);
            pst.setString(4, statut);
            pst.setDate(5, dateEmission);
            pst.setString(6, numVol);
            pst.setString(7, numSiege);
            pst.setInt(8, idBillet);
            
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du billet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour supprimer un billet
    public boolean supprimerBillet(int idBillet) {
        String sql = "DELETE FROM billet WHERE id_billet=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idBillet);
            int result = pst.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du billet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour rechercher des billets
    public List<Object[]> rechercherBillets(String recherche) {
        List<Object[]> billets = new ArrayList<>();
        String sql = "SELECT id_billet, id_passager, classe, tarif, statut, num_vol, num_siege " +
                     "FROM billet WHERE " +
                     "CAST(id_billet AS TEXT) LIKE ? OR " +
                     "CAST(id_passager AS TEXT) LIKE ? OR " +
                     "LOWER(classe) LIKE LOWER(?) OR " +
                     "LOWER(statut) LIKE LOWER(?) OR " +
                     "LOWER(num_vol) LIKE LOWER(?) OR " +
                     "LOWER(num_siege) LIKE LOWER(?) " +
                     "ORDER BY id_billet";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            String pattern = "%" + recherche + "%";
            for (int i = 1; i <= 6; i++) {
                pst.setString(i, pattern);
            }
            
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] billet = new Object[7];
                billet[0] = rs.getInt("id_billet");
                billet[1] = rs.getInt("id_passager");
                billet[2] = rs.getString("classe");
                billet[3] = rs.getDouble("tarif") + " DH";
                billet[4] = rs.getString("statut");
                billet[5] = rs.getString("num_vol");
                billet[6] = rs.getString("num_siege");
                billets.add(billet);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
        
        return billets;
    }
    
    // Méthode pour obtenir un billet par ID
    public Object[] getBilletParId(int idBillet) {
        String sql = "SELECT * FROM billet WHERE id_billet=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idBillet);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Object[] billet = new Object[8];
                billet[0] = rs.getInt("id_billet");
                billet[1] = rs.getInt("id_passager");
                billet[2] = rs.getString("classe");
                billet[3] = rs.getDouble("tarif");
                billet[4] = rs.getString("statut");
                billet[5] = rs.getDate("date_emission");
                billet[6] = rs.getString("num_vol");
                billet[7] = rs.getString("num_siege");
                return billet;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du billet: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Méthode pour obtenir les billets d'un passager
    public List<Object[]> getBilletsParPassager(int idPassager) {
        List<Object[]> billets = new ArrayList<>();
        String sql = "SELECT id_billet, id_passager, classe, tarif, statut, num_vol, num_siege " +
                     "FROM billet WHERE id_passager=? ORDER BY id_billet";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idPassager);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] billet = new Object[7];
                billet[0] = rs.getInt("id_billet");
                billet[1] = rs.getInt("id_passager");
                billet[2] = rs.getString("classe");
                billet[3] = rs.getDouble("tarif") + " DH";
                billet[4] = rs.getString("statut");
                billet[5] = rs.getString("num_vol");
                billet[6] = rs.getString("num_siege");
                billets.add(billet);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des billets: " + e.getMessage());
            e.printStackTrace();
        }
        
        return billets;
    }
    
    // Méthode pour vérifier si un passager existe
    public boolean passagerExiste(int idPassager) {
        String sql = "SELECT COUNT(*) FROM passager WHERE id_passager=?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setInt(1, idPassager);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du passager: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}

package com.compagnieaerienne.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {
    
    // Informations de connexion à PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5433/compagnie_acrienne"; // ✅ CORRIGÉ
    private static final String USER = "postgres";
    private static final String PASSWORD = "msaly2310"; // Remplacez par votre mot de passe
    
    // Méthode pour obtenir une connexion
    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver PostgreSQL
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ Driver PostgreSQL chargé");
            
            // Établir la connexion
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion BD établie : " + URL);
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver PostgreSQL non trouvé !");
            e.printStackTrace();
            throw new SQLException("Driver non trouvé", e);
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la BD : " + e.getMessage());
            throw e;
        }
    }
    
    // Méthode pour tester la connexion
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Test de connexion réussi !");
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion échoué !");
            e.printStackTrace();
        }
    }
}

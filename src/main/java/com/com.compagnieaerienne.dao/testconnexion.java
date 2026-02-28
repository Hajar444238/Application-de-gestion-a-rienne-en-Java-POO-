package com.compagnieaerienne.dao;

import java.sql.*;

public class testconnexion {
    
    public static void main(String[] args) {
        System.out.println("=== Test de connexion à la base de données ===");
        
        // Test 1: Connexion
        try (Connection conn = ConnexionBD.getConnection()) {
            System.out.println("✅ Connexion établie avec succès!");
            
            // Test 2: Vérifier le search_path
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SHOW search_path")) {
                if (rs.next()) {
                    System.out.println("\n🔍 Search path actuel: " + rs.getString(1));
                }
            }
            
            // Test 3: Lister TOUS les schémas et leurs tables
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet schemas = metaData.getSchemas();
            
            System.out.println("\n📋 Schémas et tables trouvés:");
            while (schemas.next()) {
                String schemaName = schemas.getString("TABLE_SCHEM");
                System.out.println("\n  Schéma: " + schemaName);
                
                ResultSet tables = metaData.getTables(null, schemaName, "%", new String[]{"TABLE"});
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("    - " + tableName);
                }
                tables.close();
            }
            schemas.close();
            
            // Test 4: Essayer différentes variantes de requêtes
            System.out.println("\n🔧 Test de requêtes:");
            
            String[] testQueries = {
                "SELECT COUNT(*) as total FROM passager",
                "SELECT COUNT(*) as total FROM public.passager",
                "SELECT COUNT(*) as total FROM \"passager\"",
                "SELECT COUNT(*) as total FROM public.\"passager\""
            };
            
            for (String testSql : testQueries) {
                try (PreparedStatement pst = conn.prepareStatement(testSql);
                     ResultSet rs = pst.executeQuery()) {
                    
                    if (rs.next()) {
                        int total = rs.getInt("total");
                        System.out.println("  ✅ Réussi: " + testSql + " → " + total + " passagers");
                        
                        // Si cette requête fonctionne, afficher quelques passagers
                        String selectSql = testSql.replace("COUNT(*) as total", "id_passager, nom, prenom").replace("SELECT", "SELECT").concat(" LIMIT 3");
                        try (PreparedStatement pst2 = conn.prepareStatement(selectSql);
                             ResultSet rs2 = pst2.executeQuery()) {
                            
                            System.out.println("    📊 Exemples de passagers:");
                            while (rs2.next()) {
                                System.out.printf("      ID: %d - %s %s%n", 
                                    rs2.getInt("id_passager"),
                                    rs2.getString("nom"),
                                    rs2.getString("prenom"));
                            }
                        }
                        break; // Sortir de la boucle si une requête fonctionne
                    }
                } catch (SQLException e) {
                    System.out.println("  ❌ Échoué: " + testSql + " → " + e.getMessage());
                }
            }
            
            System.out.println("\n✅ Test terminé!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

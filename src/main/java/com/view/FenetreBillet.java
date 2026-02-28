package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.compagnieaerienne.dao.BilletDAO;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.List;

public class FenetreBillet extends JFrame {
    
    private JTextField txtIdPassager, txtNumBillet, txtTarif, txtNumVol, txtNumSiege, txtRecherche;
    private JComboBox<String> cmbClasse, cmbStatut;
    private JSpinner spnDateEmission;
    private JTable tableBillets;
    private DefaultTableModel tableModel;
    private JLabel lblStats;
    private BilletDAO billetDAO = new BilletDAO();
    
    public FenetreBillet() {
        setTitle("Gestion des Billets");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 240, 241));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Titre
        JLabel lblTitre = new JLabel("Gestion des Billets", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setForeground(new Color(52, 73, 94));
        mainPanel.add(lblTitre, BorderLayout.NORTH);
        
        // Panel formulaire
        JPanel formPanel = createFormPanel();
        
        // Panel tableau
        JPanel tablePanel = createTablePanel();
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0.4);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Charger les billets au démarrage
        chargerBillets();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Informations du Billet",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(46, 204, 113)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ID Passager
        addFormField(panel, gbc, 0, "ID Passager:", txtIdPassager = createTextField());
        
        // Numéro Billet
        addFormField(panel, gbc, 1, "N° Billet:", txtNumBillet = createTextField());
        txtNumBillet.setEditable(false);
        txtNumBillet.setBackground(new Color(245, 245, 245));
        txtNumBillet.setToolTipText("Numéro auto-généré");
        
        // Classe
        String[] classes = {"Première", "Business", "Économique"};
        addFormField(panel, gbc, 2, "Classe:", cmbClasse = new JComboBox<>(classes));
        styleComboBox(cmbClasse);
        
        // Tarif
        addFormField(panel, gbc, 3, "Tarif (DH):", txtTarif = createTextField());
        
        // Statut
        String[] statuts = {"Réservé", "Annulé", "Remboursé", "Modifié"};
        addFormField(panel, gbc, 4, "Statut:", cmbStatut = new JComboBox<>(statuts));
        styleComboBox(cmbStatut);
        
        // Date d'émission
        addFormField(panel, gbc, 5, "Date Émission:", 
            spnDateEmission = new JSpinner(new SpinnerDateModel()));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnDateEmission, "dd/MM/yyyy");
        spnDateEmission.setEditor(editor);
        spnDateEmission.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Numéro Vol
        addFormField(panel, gbc, 6, "N° Vol:", txtNumVol = createTextField());
        
        // Numéro Siège
        addFormField(panel, gbc, 7, "N° Siège:", txtNumSiege = createTextField());
        
        // Panel d'information sur les tarifs
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(241, 248, 233));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(139, 195, 74)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblInfo = new JLabel("💡 Tarifs Indicatifs:");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(lblInfo);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(new JLabel("Économique: 500-1500 DH"));
        infoPanel.add(new JLabel("Business: 2000-4000 DH"));
        infoPanel.add(new JLabel("Première: 5000-8000 DH"));
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(infoPanel, gbc);
        
        // Boutons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        
        JButton btnAjouter = createActionButton("Ajouter", new Color(46, 204, 113));
        JButton btnModifier = createActionButton("Modifier", new Color(52, 152, 219));
        JButton btnSupprimer = createActionButton("Supprimer", new Color(231, 76, 60));
        JButton btnNouveau = createActionButton("Nouveau", new Color(149, 165, 166));
        
        btnAjouter.addActionListener(e -> ajouterBillet());
        btnModifier.addActionListener(e -> modifierBillet());
        btnSupprimer.addActionListener(e -> supprimerBillet());
        btnNouveau.addActionListener(e -> nouveauBillet());
        
        btnPanel.add(btnAjouter);
        btnPanel.add(btnModifier);
        btnPanel.add(btnSupprimer);
        btnPanel.add(btnNouveau);
        
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(btnPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Liste des Billets",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(46, 204, 113)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        String[] columns = {"N° Billet", "ID Pass.", "Classe", "Tarif", "Statut", "Vol", "Siège"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableBillets = new JTable(tableModel);
        tableBillets.setFont(new Font("Arial", Font.PLAIN, 12));
        tableBillets.setRowHeight(30);
        tableBillets.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableBillets.getTableHeader().setBackground(new Color(46, 204, 113));
        tableBillets.getTableHeader().setForeground(Color.WHITE);
        tableBillets.setSelectionBackground(new Color(174, 214, 241));
        tableBillets.setSelectionForeground(Color.BLACK);
        tableBillets.setGridColor(new Color(189, 195, 199));
        
        // Double-clic pour charger dans le formulaire
        tableBillets.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerBilletSelectionne();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableBillets);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Barre de recherche et statistiques
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setOpaque(false);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        
        txtRecherche = createTextField();
        txtRecherche.setPreferredSize(new Dimension(200, 30));
        
        JButton btnRechercher = createActionButton("🔍 Rechercher", new Color(52, 152, 219));
        btnRechercher.addActionListener(e -> rechercherBillet());
        
        // Bouton Actualiser
        JButton btnActualiser = createActionButton("🔄 Actualiser", new Color(46, 204, 113));
        btnActualiser.addActionListener(e -> chargerBillets());
        
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(txtRecherche);
        searchPanel.add(btnRechercher);
        searchPanel.add(btnActualiser);
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setOpaque(false);
        lblStats = new JLabel("Total: 0 billets");
        lblStats.setFont(new Font("Arial", Font.BOLD, 12));
        lblStats.setForeground(new Color(52, 73, 94));
        statsPanel.add(lblStats);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                              String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(52, 73, 94));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
    
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }
    
    private void styleComboBox(JComboBox<String> cmb) {
        cmb.setFont(new Font("Arial", Font.PLAIN, 13));
        cmb.setBackground(Color.WHITE);
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    // ==================== MÉTHODES CRUD ====================
    
    private void ajouterBillet() {
        if (txtIdPassager.getText().trim().isEmpty() || txtTarif.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs obligatoires !", 
                "Champs obligatoires", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idPassager = Integer.parseInt(txtIdPassager.getText().trim());
            
            if (!billetDAO.passagerExiste(idPassager)) {
                JOptionPane.showMessageDialog(this, 
                    "Le passager avec l'ID " + idPassager + " n'existe pas !\n" +
                    "Veuillez d'abord créer le passager.", 
                    "Passager inexistant", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String classe = (String) cmbClasse.getSelectedItem();
            double tarif = Double.parseDouble(txtTarif.getText().trim());
            String statut = (String) cmbStatut.getSelectedItem();
            
            java.util.Date dateUtil = (java.util.Date) spnDateEmission.getValue();
            Date dateEmission = new Date(dateUtil.getTime());
            
            String numVol = txtNumVol.getText().trim();
            String numSiege = txtNumSiege.getText().trim();
            
            boolean success = billetDAO.ajouterBillet(idPassager, classe, tarif, statut, 
                                                      dateEmission, numVol, numSiege);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Billet ajouté avec succès !", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                chargerBillets();
                nouveauBillet();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ajout du billet !", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur de format:\n" +
                "- L'ID Passager doit être un nombre entier\n" +
                "- Le Tarif doit être un nombre décimal", 
                "Erreur de saisie", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void modifierBillet() {
        if (txtNumBillet.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un billet dans le tableau !", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int idBillet = Integer.parseInt(txtNumBillet.getText().trim());
            int idPassager = Integer.parseInt(txtIdPassager.getText().trim());
            
            if (!billetDAO.passagerExiste(idPassager)) {
                JOptionPane.showMessageDialog(this, 
                    "Le passager avec l'ID " + idPassager + " n'existe pas !", 
                    "Passager inexistant", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String classe = (String) cmbClasse.getSelectedItem();
            double tarif = Double.parseDouble(txtTarif.getText().trim());
            String statut = (String) cmbStatut.getSelectedItem();
            
            java.util.Date dateUtil = (java.util.Date) spnDateEmission.getValue();
            Date dateEmission = new Date(dateUtil.getTime());
            
            String numVol = txtNumVol.getText().trim();
            String numSiege = txtNumSiege.getText().trim();
            
            boolean success = billetDAO.modifierBillet(idBillet, idPassager, classe, tarif,
                                                       statut, dateEmission, numVol, numSiege);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Billet modifié avec succès !", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                chargerBillets();
                nouveauBillet();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification !", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur de format dans les champs numériques !", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void supprimerBillet() {
        if (txtNumBillet.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un billet dans le tableau !", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer ce billet ?\n" +
            "Billet N°: " + txtNumBillet.getText(), 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idBillet = Integer.parseInt(txtNumBillet.getText().trim());
                boolean success = billetDAO.supprimerBillet(idBillet);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Billet supprimé avec succès !", 
                        "Suppression", 
                        JOptionPane.INFORMATION_MESSAGE);
                    chargerBillets();
                    nouveauBillet();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Erreur lors de la suppression !", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur: " + ex.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void chargerBillets() {
        try {
            tableModel.setRowCount(0);
            
            List<Object[]> billets = billetDAO.getTousLesBillets();
            
            for (Object[] billet : billets) {
                tableModel.addRow(billet);
            }
            
            lblStats.setText("Total: " + tableModel.getRowCount() + " billets");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des billets: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void rechercherBillet() {
        String recherche = txtRecherche.getText().trim();
        
        if (recherche.isEmpty()) {
            chargerBillets();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            
            List<Object[]> billets = billetDAO.rechercherBillets(recherche);
            
            for (Object[] billet : billets) {
                tableModel.addRow(billet);
            }
            
            lblStats.setText("Total: " + tableModel.getRowCount() + " billets");
            
            if (billets.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucun billet trouvé pour: " + recherche, 
                    "Recherche", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la recherche: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void nouveauBillet() {
        txtIdPassager.setText("");
        txtNumBillet.setText("");
        txtTarif.setText("");
        txtNumVol.setText("");
        txtNumSiege.setText("");
        cmbClasse.setSelectedIndex(0);
        cmbStatut.setSelectedIndex(0);
        spnDateEmission.setValue(new java.util.Date());
        
        txtIdPassager.requestFocus();
    }
    
    private void chargerBilletSelectionne() {
        int row = tableBillets.getSelectedRow();
        if (row != -1) {
            txtNumBillet.setText(tableModel.getValueAt(row, 0).toString());
            txtIdPassager.setText(tableModel.getValueAt(row, 1).toString());
            cmbClasse.setSelectedItem(tableModel.getValueAt(row, 2).toString());
            
            String tarifStr = tableModel.getValueAt(row, 3).toString();
            txtTarif.setText(tarifStr.replace(" DH", "").trim());
            
            cmbStatut.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            txtNumVol.setText(tableModel.getValueAt(row, 5).toString());
            txtNumSiege.setText(tableModel.getValueAt(row, 6).toString());
        }
    }
}

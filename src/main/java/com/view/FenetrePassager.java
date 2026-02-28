package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import com.compagnieaerienne.dao.PassagerDAO;
import java.sql.Date;
import java.util.List;

public class FenetrePassager extends JFrame {
    
    private JTextField txtId, txtNom, txtPrenom, txtPasseport, txtEmail, txtTelephone, txtRecherche;
    private JComboBox<String> cmbNationalite, cmbTypePassager, cmbGenre;
    private JSpinner spnDateNaissance;
    private JTable tablePassagers;
    private DefaultTableModel tableModel;
    private JLabel lblTotalPassagers;
    private PassagerDAO passagerDAO = new PassagerDAO();
    
    public FenetrePassager() {
        setTitle("Gestion des Passagers");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal avec dégradé
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(240, 242, 245);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header avec titre et icône
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel formulaire
        JPanel formPanel = createFormPanel();
        
        // Panel tableau
        JPanel tablePanel = createTablePanel();
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(480);
        splitPane.setResizeWeight(0.4);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Charger les passagers au démarrage
        chargerPassagers();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);
        
        // Icône
        JLabel iconLabel = new JLabel("✈️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        
        // Titre et sous-titre
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel lblTitre = new JLabel("Gestion des Passagers");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(new Color(44, 62, 80));
        
        JLabel lblSousTitre = new JLabel("Gérez les informations des passagers de votre compagnie");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSousTitre.setForeground(new Color(127, 140, 141));
        
        textPanel.add(lblTitre);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(lblSousTitre);
        
        titlePanel.add(iconLabel);
        titlePanel.add(textPanel);
        
        panel.add(titlePanel, BorderLayout.WEST);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 232), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Titre de la section
        JLabel lblSectionTitle = new JLabel("📋 Formulaire Passager");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitle.setForeground(new Color(52, 152, 219));
        lblSectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(lblSectionTitle, BorderLayout.NORTH);
        
        // Panel des champs
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        // ID (non-éditable, auto-généré)
        txtId = createTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(245, 245, 245));
        txtId.setForeground(new Color(120, 120, 120));
        txtId.setToolTipText("ID auto-généré par la base de données");
        addFormField(fieldsPanel, gbc, 0, "🆔 ID (auto):", txtId);
        
        // Nom
        addFormField(fieldsPanel, gbc, 1, "👤 Nom:", txtNom = createTextField());
        
        // Prénom
        addFormField(fieldsPanel, gbc, 2, "👤 Prénom:", txtPrenom = createTextField());
        
        // Date de naissance
        spnDateNaissance = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnDateNaissance, "dd/MM/yyyy");
        spnDateNaissance.setEditor(editor);
        spnDateNaissance.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spnDateNaissance.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 232)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        addFormField(fieldsPanel, gbc, 3, "📅 Date de Naissance:", spnDateNaissance);
        
        // Passeport
        addFormField(fieldsPanel, gbc, 4, "🛂 N° Passeport:", txtPasseport = createTextField());
        
        // Nationalité
        String[] nationalites = {"Marocaine", "Française", "Espagnole", "Américaine", "Britannique", 
                                 "Allemande", "Italienne", "Canadienne", "Autre"};
        cmbNationalite = new JComboBox<>(nationalites);
        styleComboBox(cmbNationalite);
        addFormField(fieldsPanel, gbc, 5, "🌍 Nationalité:", cmbNationalite);
        
        // Type Passager
        String[] types = {"Adulte", "Enfant", "Bébé", "Senior"};
        cmbTypePassager = new JComboBox<>(types);
        styleComboBox(cmbTypePassager);
        addFormField(fieldsPanel, gbc, 6, "👥 Type Passager:", cmbTypePassager);
        
        // Genre
        String[] genres = {"Masculin", "Féminin"};
        cmbGenre = new JComboBox<>(genres);
        styleComboBox(cmbGenre);
        addFormField(fieldsPanel, gbc, 7, "⚧ Genre:", cmbGenre);
        
        // Email
        addFormField(fieldsPanel, gbc, 8, "📧 Email:", txtEmail = createTextField());
        
        // Téléphone
        addFormField(fieldsPanel, gbc, 9, "📱 Téléphone:", txtTelephone = createTextField());
        
        JScrollPane scrollPane = new JScrollPane(fieldsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Boutons d'action
        JPanel btnPanel = createButtonPanel();
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnAjouter = createModernButton("➕ Ajouter", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton btnModifier = createModernButton("✏️ Modifier", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton btnSupprimer = createModernButton("🗑️ Supprimer", new Color(231, 76, 60), new Color(192, 57, 43));
        JButton btnNouveau = createModernButton("🆕 Nouveau", new Color(149, 165, 166), new Color(127, 140, 141));
        
        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterPassager());
        btnModifier.addActionListener(e -> modifierPassager());
        btnSupprimer.addActionListener(e -> supprimerPassager());
        btnNouveau.addActionListener(e -> nouveauPassager());
        
        panel.add(btnAjouter);
        panel.add(btnModifier);
        panel.add(btnSupprimer);
        panel.add(btnNouveau);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 232), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // En-tête avec recherche
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        
        JLabel lblSectionTitle = new JLabel("📊 Liste des Passagers");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitle.setForeground(new Color(52, 152, 219));
        topPanel.add(lblSectionTitle, BorderLayout.NORTH);
        
        // Barre de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        txtRecherche = createTextField();
        txtRecherche.setPreferredSize(new Dimension(250, 38));
        
        JButton btnRechercher = createModernButton("🔍 Rechercher", new Color(52, 152, 219), new Color(41, 128, 185));
        btnRechercher.setPreferredSize(new Dimension(140, 38));
        btnRechercher.addActionListener(e -> rechercherPassager());
        
        JButton btnActualiser = createModernButton("🔄 Actualiser", new Color(52, 73, 94), new Color(44, 62, 80));
        btnActualiser.setPreferredSize(new Dimension(140, 38));
        btnActualiser.addActionListener(e -> chargerPassagers());
        
        JPanel searchFieldPanel = new JPanel(new BorderLayout(5, 0));
        searchFieldPanel.setOpaque(false);
        searchFieldPanel.add(new JLabel("🔎"), BorderLayout.WEST);
        searchFieldPanel.add(txtRecherche, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnRechercher);
        buttonsPanel.add(btnActualiser);
        
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        searchPanel.add(buttonsPanel, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tableau
        String[] columns = {"ID", "Nom", "Prénom", "Passeport", "Type", "Nationalité", "Email", "Téléphone"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablePassagers = new JTable(tableModel);
        tablePassagers.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablePassagers.setRowHeight(35);
        tablePassagers.setShowGrid(true);
        tablePassagers.setGridColor(new Color(236, 240, 241));
        tablePassagers.setSelectionBackground(new Color(52, 152, 219, 40));
        tablePassagers.setSelectionForeground(new Color(44, 62, 80));
        tablePassagers.setIntercellSpacing(new Dimension(10, 10));
        
        // En-tête du tableau
        tablePassagers.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablePassagers.getTableHeader().setBackground(new Color(52, 152, 219));
        tablePassagers.getTableHeader().setForeground(Color.WHITE);
        tablePassagers.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Centrer le contenu des cellules
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablePassagers.getColumnCount(); i++) {
            tablePassagers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Double-clic pour charger dans le formulaire
        tablePassagers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    chargerPassagerSelectionne();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablePassagers);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 232)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer avec statistiques
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(new Color(250, 250, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        lblTotalPassagers = new JLabel("📊 Total: 0 passager(s)");
        lblTotalPassagers.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalPassagers.setForeground(new Color(52, 73, 94));
        footerPanel.add(lblTotalPassagers);
        
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(52, 73, 94));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
    
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 232)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Effet focus
        txt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txt.isEditable()) {
                    txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
            }
            public void focusLost(FocusEvent e) {
                txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 226, 232)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return txt;
    }
    
    private void styleComboBox(JComboBox<String> cmb) {
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmb.setBackground(Color.WHITE);
        cmb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 232)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
    
    private JButton createModernButton(String text, Color color1, Color color2) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(color2);
                } else if (getModel().isRollover()) {
                    g2d.setColor(color1.brighter());
                } else {
                    g2d.setColor(color1);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    // ==================== MÉTHODES CRUD ====================
    
    private void ajouterPassager() {
        if (txtNom.getText().trim().isEmpty() || txtPrenom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir au moins le nom et le prénom !", 
                "Champs obligatoires", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            
            java.util.Date dateUtil = (java.util.Date) spnDateNaissance.getValue();
            Date dateNaissance = new Date(dateUtil.getTime());
            
            String passeport = txtPasseport.getText().trim();
            String nationalite = (String) cmbNationalite.getSelectedItem();
            String typePassager = (String) cmbTypePassager.getSelectedItem();
            String genre = (String) cmbGenre.getSelectedItem();
            String email = txtEmail.getText().trim();
            String telephone = txtTelephone.getText().trim();
            
            boolean success = passagerDAO.ajouterPassager(nom, prenom, dateNaissance, 
                                                          passeport, nationalite, typePassager, 
                                                          genre, email, telephone);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Passager ajouté avec succès !", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                chargerPassagers();
                nouveauPassager();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ajout du passager !", 
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
    
    private void modifierPassager() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un passager dans le tableau !", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            
            java.util.Date dateUtil = (java.util.Date) spnDateNaissance.getValue();
            Date dateNaissance = new Date(dateUtil.getTime());
            
            String passeport = txtPasseport.getText().trim();
            String nationalite = (String) cmbNationalite.getSelectedItem();
            String typePassager = (String) cmbTypePassager.getSelectedItem();
            String genre = (String) cmbGenre.getSelectedItem();
            String email = txtEmail.getText().trim();
            String telephone = txtTelephone.getText().trim();
            
            boolean success = passagerDAO.modifierPassager(id, nom, prenom, dateNaissance,
                                                           passeport, nationalite, typePassager,
                                                           genre, email, telephone);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Passager modifié avec succès !", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                chargerPassagers();
                nouveauPassager();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification !", 
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
    
    private void supprimerPassager() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un passager dans le tableau !", 
                "Aucune sélection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer le passager:\n" + 
            txtNom.getText() + " " + txtPrenom.getText() + " ?", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                boolean success = passagerDAO.supprimerPassager(id);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Passager supprimé avec succès !", 
                        "Suppression", 
                        JOptionPane.INFORMATION_MESSAGE);
                    chargerPassagers();
                    nouveauPassager();
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
    
    private void chargerPassagers() {
        try {
            tableModel.setRowCount(0);
            
            List<Object[]> passagers = passagerDAO.getTousLesPassagers();
            
            for (Object[] passager : passagers) {
                tableModel.addRow(passager);
            }
            
            lblTotalPassagers.setText("📊 Total: " + tableModel.getRowCount() + " passager(s)");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des passagers: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void rechercherPassager() {
        String recherche = txtRecherche.getText().trim();
        
        if (recherche.isEmpty()) {
            chargerPassagers();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            
            List<Object[]> passagers = passagerDAO.rechercherPassagers(recherche);
            
            for (Object[] passager : passagers) {
                tableModel.addRow(passager);
            }
            
            lblTotalPassagers.setText("📊 Total: " + tableModel.getRowCount() + " passager(s)");
            
            if (passagers.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucun passager trouvé pour: " + recherche, 
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
    
    private void nouveauPassager() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtPasseport.setText("");
        txtEmail.setText("");
        txtTelephone.setText("");
        cmbNationalite.setSelectedIndex(0);
        cmbTypePassager.setSelectedIndex(0);
        cmbGenre.setSelectedIndex(0);
        spnDateNaissance.setValue(new java.util.Date());
        
        txtNom.requestFocus();
    }
    
    private void chargerPassagerSelectionne() {
        int row = tablePassagers.getSelectedRow();
        if (row != -1) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNom.setText(tableModel.getValueAt(row, 1).toString());
            txtPrenom.setText(tableModel.getValueAt(row, 2).toString());
            txtPasseport.setText(tableModel.getValueAt(row, 3).toString());
            cmbTypePassager.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            cmbNationalite.setSelectedItem(tableModel.getValueAt(row, 5).toString());
            txtEmail.setText(tableModel.getValueAt(row, 6).toString());
            txtTelephone.setText(tableModel.getValueAt(row, 7).toString());
        }
    }
}

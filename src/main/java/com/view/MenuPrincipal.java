package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {
    
    public MenuPrincipal() {
        setTitle("Gestion Compagnie Aérienne");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal avec dégradé
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(109, 213, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        
        // Titre
        JLabel lblTitre = new JLabel("Système de Gestion Aérienne", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        mainPanel.add(lblTitre, BorderLayout.NORTH);
        
        // Panel central pour les boutons
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 50, 15, 50);
        
        // Bouton Passagers
        JButton btnPassager = createStyledButton("✈ Gestion des Passagers", 
            new Color(52, 152, 219), new Color(41, 128, 185));
        btnPassager.addActionListener(e -> {
            new FenetrePassager().setVisible(true);
        });
        centerPanel.add(btnPassager, gbc);
        
        // Bouton Billets
        JButton btnBillet = createStyledButton("🎫 Gestion des Billets", 
            new Color(46, 204, 113), new Color(39, 174, 96));
        btnBillet.addActionListener(e -> {
            new FenetreBillet().setVisible(true);
        });
        centerPanel.add(btnBillet, gbc);
        
        // Bouton Quitter
        JButton btnQuitter = createStyledButton("❌ Quitter", 
            new Color(231, 76, 60), new Color(192, 57, 43));
        btnQuitter.addActionListener(e -> System.exit(0));
        centerPanel.add(btnQuitter, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel lblFooter = new JLabel("© 2025 - Projet POO Java", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFooter.setForeground(Color.WHITE);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        mainPanel.add(lblFooter, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                
                if (getModel().isPressed()) {
                    g2d.setColor(color2);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, color1.brighter(), 0, h, color2.brighter());
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                    g2d.setPaint(gp);
                }
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(400, 70));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MenuPrincipal().setVisible(true);
        });
    }
}

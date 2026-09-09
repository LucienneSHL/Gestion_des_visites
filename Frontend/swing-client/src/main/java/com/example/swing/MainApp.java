package com.example.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.swing.api.ApiClient;
import com.example.swing.ui.MedecinPanel;
import com.example.swing.ui.PatientPanel;
import com.example.swing.ui.VisitePanel;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

public class MainApp {

    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel headerPanel;
    private static JPanel sidebar;
    private static JPanel contentPanel;
    private static JButton btnTheme;
    private static JLabel titleLabel;
    private static JLabel statusDot;
    private static JLabel statusLabel;
    private static JLabel logoLabel;
    private static JSeparator separator;
    
    private static MedecinPanel medecinPanel;
    private static PatientPanel patientPanel;
    private static VisitePanel visitePanel;
    
    // ✅ Stocker les boutons de navigation pour les mettre à jour
    private static java.util.List<JButton> navButtons = new java.util.ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
                ThemeManager.setDarkMode(true);
                
                UIManager.put("Button.arc", 8);
                UIManager.put("Component.arc", 8);
                UIManager.put("TextComponent.arc", 8);
                UIManager.put("Table.arc", 8);
                UIManager.put("TabbedPane.arc", 8);
                UIManager.put("ScrollBar.arc", 8);
            } catch (Exception e) {
                e.printStackTrace();
            }

            testBackendConnection();

            frame = new JFrame("Gestion des Visites Medicales");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(ThemeManager.getBackgroundColor());

            headerPanel = createHeader();
            mainPanel.add(headerPanel, BorderLayout.NORTH);

            contentPanel = new JPanel(new CardLayout());
            contentPanel.setBackground(ThemeManager.getBackgroundColor());

            medecinPanel = new MedecinPanel();
            patientPanel = new PatientPanel();
            visitePanel = new VisitePanel();

            contentPanel.add(medecinPanel, "medecins");
            contentPanel.add(patientPanel, "patients");
            contentPanel.add(visitePanel, "visites");

            sidebar = createModernSidebar(frame, contentPanel);
            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);

            frame.add(mainPanel);
            frame.setVisible(true);

            showPanel(contentPanel, "medecins");
            
            SwingUtilities.invokeLater(() -> {
                updateAllComponents();
            });
        });
    }

    public static void toggleTheme() {
        try {
            ThemeManager.toggleMode();
            
            if (ThemeManager.isDarkMode()) {
                UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            
            updateAllComponents();
            
            SwingUtilities.updateComponentTreeUI(frame);
            frame.revalidate();
            frame.repaint();
            
            SwingUtilities.invokeLater(() -> {
                if (medecinPanel != null) {
                    medecinPanel.revalidate();
                    medecinPanel.repaint();
                }
                if (patientPanel != null) {
                    patientPanel.revalidate();
                    patientPanel.repaint();
                }
                if (visitePanel != null) {
                    visitePanel.revalidate();
                    visitePanel.repaint();
                }
                contentPanel.revalidate();
                contentPanel.repaint();
                sidebar.revalidate();
                sidebar.repaint();
                headerPanel.revalidate();
                headerPanel.repaint();
                mainPanel.revalidate();
                mainPanel.repaint();
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "Erreur lors du changement de theme: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateAllComponents() {
        // ✅ 1. Panels principaux
        mainPanel.setBackground(ThemeManager.getBackgroundColor());
        contentPanel.setBackground(ThemeManager.getBackgroundColor());
        sidebar.setBackground(ThemeManager.getSidebarBackground());
        headerPanel.setBackground(ThemeManager.getHeaderColor());
        
        // ✅ 2. Header
        if (titleLabel != null) {
            titleLabel.setForeground(Color.WHITE);
        }
        if (statusDot != null) {
            statusDot.setForeground(new Color(0, 200, 83));
        }
        if (statusLabel != null) {
            statusLabel.setForeground(new Color(200, 200, 230));
        }
        
        // ✅ 3. Sidebar - Mettre à jour TOUS les boutons
        updateSidebarButtons();
        
        // ✅ 4. Bouton thème
        if (btnTheme != null) {
            if (ThemeManager.isDarkMode()) {
                btnTheme.setText("☀️");
                btnTheme.setToolTipText("Passer en mode clair");
            } else {
                btnTheme.setText("🌙");
                btnTheme.setToolTipText("Passer en mode sombre");
            }
        }
        
        // ✅ 5. Panels enfants
        if (medecinPanel != null) {
            medecinPanel.updateTheme();
            medecinPanel.revalidate();
            medecinPanel.repaint();
        }
        if (patientPanel != null) {
            patientPanel.updateTheme();
            patientPanel.revalidate();
            patientPanel.repaint();
        }
        if (visitePanel != null) {
            visitePanel.updateTheme();
            visitePanel.revalidate();
            visitePanel.repaint();
        }
        
        frame.repaint();
    }
    
    // ✅ Méthode pour mettre à jour les boutons de la sidebar
    private static void updateSidebarButtons() {
        Color bgColor = ThemeManager.isDarkMode() ? new Color(50, 54, 67) : new Color(240, 242, 245);
        Color fgColor = ThemeManager.isDarkMode() ? new Color(220, 220, 230) : new Color(30, 34, 45);
        Color hoverBg = ThemeManager.isDarkMode() ? new Color(70, 74, 90) : new Color(220, 222, 225);
        
        if (logoLabel != null) {
            logoLabel.setForeground(ThemeManager.getMenuColor());
        }
        if (separator != null) {
            separator.setForeground(ThemeManager.getSeparatorColor());
        }
        
        // ✅ Mettre à jour tous les boutons de la sidebar
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(bgColor);
                btn.setForeground(fgColor);
                
                // ✅ Mettre à jour les MouseListeners
                for (java.awt.event.MouseListener ml : btn.getMouseListeners()) {
                    btn.removeMouseListener(ml);
                }
                
                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        btn.setBackground(hoverBg);
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        btn.setBackground(bgColor);
                    }
                });
                
                btn.revalidate();
                btn.repaint();
            }
        }
        sidebar.revalidate();
        sidebar.repaint();
    }

    private static void testBackendConnection() {
        try {
            System.out.println("Test de connexion au backend...");
            ApiClient testClient = new ApiClient();
            String response = testClient.get("/medecins");
            System.out.println("Connexion reussie !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
        }
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ThemeManager.getHeaderColor());
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        titleLabel = new JLabel("Gestion des Visites Medicales");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);

        statusDot = new JLabel("●");
        statusDot.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statusDot.setForeground(new Color(0, 200, 83));

        statusLabel = new JLabel("Connecte");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 200, 230));

        rightPanel.add(statusDot);
        rightPanel.add(statusLabel);

        btnTheme = new JButton("☀️");
        btnTheme.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnTheme.setBackground(new Color(255, 255, 255, 50));
        btnTheme.setForeground(Color.WHITE);
        btnTheme.setFocusPainted(false);
        btnTheme.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTheme.setToolTipText("Changer de theme");
        
        btnTheme.addActionListener(e -> {
            toggleTheme();
        });

        rightPanel.add(btnTheme);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private static JPanel createModernSidebar(JFrame frame, JPanel contentPanel) {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(ThemeManager.getSidebarBackground());
        sidebar.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.insets = new Insets(30, 15, 20, 15);
        logoLabel = new JLabel("VISITE MEDICAL");
        logoLabel.setForeground(ThemeManager.getMenuColor());
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        sidebar.add(logoLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 15, 15, 15);
        separator = new JSeparator();
        separator.setForeground(ThemeManager.getSeparatorColor());
        sidebar.add(separator, gbc);

        String[][] buttons = {
            {"Médecins", "medecins"},
            {"Patients", "patients"},
            {"Visites", "visites"}
        };

        gbc.insets = new Insets(3, 15, 3, 15);
        for (int i = 0; i < buttons.length; i++) {
            gbc.gridy = i + 2;
            JButton btn = createNavButton(buttons[i][0], buttons[i][1], contentPanel);
            sidebar.add(btn, gbc);
            navButtons.add(btn);
        }

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        sidebar.add(Box.createVerticalGlue(), gbc);

        gbc.gridy = 6;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 15, 10, 15);
        JButton btnRefresh = createNavButton("Rafraichir", null, null);
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.addActionListener(e -> {
            System.out.println("Rafraichissement...");
            for (Component comp : contentPanel.getComponents()) {
                if (comp instanceof MedecinPanel) {
                    ((MedecinPanel) comp).chargerListe();
                } else if (comp instanceof PatientPanel) {
                    ((PatientPanel) comp).chargerListe();
                } else if (comp instanceof VisitePanel) {
                    ((VisitePanel) comp).chargerListe();
                }
            }
            JOptionPane.showMessageDialog(frame,
                "Toutes les donnees ont ete rafraichies !",
                "Rafraichissement",
                JOptionPane.INFORMATION_MESSAGE);
        });
        sidebar.add(btnRefresh, gbc);
        navButtons.add(btnRefresh);

        return sidebar;
    }

    private static JButton createNavButton(String text, String panelName, JPanel contentPanel) {
        JButton button = new JButton(text);
        Color bgColor = ThemeManager.isDarkMode() ? new Color(50, 54, 67) : new Color(240, 242, 245);
        Color fgColor = ThemeManager.isDarkMode() ? new Color(220, 220, 230) : new Color(30, 34, 45);
        Color hoverBg = ThemeManager.isDarkMode() ? new Color(70, 74, 90) : new Color(220, 222, 225);
        
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(200, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBg);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        if (panelName != null && contentPanel != null) {
            button.addActionListener(e -> showPanel(contentPanel, panelName));
        }

        return button;
    }

    private static void showPanel(JPanel contentPanel, String name) {
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, name);
    }
}
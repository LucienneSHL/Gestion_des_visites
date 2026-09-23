package com.example.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.swing.ui.MedecinPanel;
import com.example.swing.ui.PatientPanel;
import com.example.swing.ui.VisitePanel;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

public class MainApp {

    private static JFrame frame;
    private static JPanel headerPanel;
    private static JPanel sidebar;
    private static JPanel contentPanel;
    private static CardLayout cardLayout;
    private static JButton btnTheme;
    private static JLabel titleLabel;
    private static JLabel logoLabel;

    private static MedecinPanel medecinPanel;
    private static PatientPanel patientPanel;
    private static VisitePanel visitePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
                ThemeManager.setDarkMode(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame = new JFrame("Gestion des Visites Médicales");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(ThemeManager.getBackgroundColor());

            headerPanel = createHeader();
            mainPanel.add(headerPanel, BorderLayout.NORTH);

            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);

            medecinPanel = new MedecinPanel();
            patientPanel = new PatientPanel();
            visitePanel = new VisitePanel();

            contentPanel.add(medecinPanel, "medecins");
            contentPanel.add(patientPanel, "patients");
            contentPanel.add(visitePanel, "visites");

            sidebar = createSidebar();
            mainPanel.add(sidebar, BorderLayout.WEST);
            mainPanel.add(contentPanel, BorderLayout.CENTER);

            frame.add(mainPanel);
            frame.setVisible(true);

            showPanel("medecins");
        });
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ThemeManager.getHeaderColor());
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        titleLabel = new JLabel("Gestion des Visites Médicales");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);

        JLabel statusDot = new JLabel("o");
        statusDot.setForeground(new Color(0, 200, 83));

        JLabel statusLabel = new JLabel("Connecte");
        statusLabel.setForeground(new Color(200, 200, 230));

        rightPanel.add(statusDot);
        rightPanel.add(statusLabel);

        btnTheme = new JButton("Theme");
        btnTheme.setForeground(Color.WHITE);
        btnTheme.setFocusPainted(false);
        btnTheme.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTheme.addActionListener(e -> toggleTheme());
        rightPanel.add(btnTheme);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private static JPanel createSidebar() {
        JPanel sidebarLocal = new JPanel();
        sidebarLocal.setPreferredSize(new Dimension(240, 0));
        sidebarLocal.setBackground(ThemeManager.getSidebarBackground());
        sidebarLocal.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.insets = new Insets(30, 15, 20, 15);
        logoLabel = new JLabel("VISITE MEDICAL");
        logoLabel.setForeground(ThemeManager.getMenuColor());
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sidebarLocal.add(logoLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 15, 15, 15);
        JSeparator separator = new JSeparator();
        sidebarLocal.add(separator, gbc);

        gbc.insets = new Insets(3, 15, 3, 15);

        gbc.gridy = 2;
        sidebarLocal.add(createNavButton("Medecins", "medecins"), gbc);

        gbc.gridy = 3;
        sidebarLocal.add(createNavButton("Patients", "patients"), gbc);

        gbc.gridy = 4;
        sidebarLocal.add(createNavButton("Visites", "visites"), gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        sidebarLocal.add(Box.createVerticalGlue(), gbc);

        gbc.gridy = 6;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 15, 10, 15);
        JButton btnRefresh = new JButton("Rafraichir");
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setPreferredSize(new Dimension(200, 42));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> {
            System.out.println("=== Rafraichissement ===");
            if (medecinPanel != null) medecinPanel.chargerListe();
            if (patientPanel != null) patientPanel.chargerListe();
            if (visitePanel != null) visitePanel.chargerListe();
        });
        sidebarLocal.add(btnRefresh, gbc);

        return sidebarLocal;
    }

    private static JButton createNavButton(String text, final String panelName) {
        JButton button = new JButton(text);
        Color bgColor = new Color(50, 54, 67);
        Color fgColor = new Color(220, 220, 230);
        Color hoverBg = new Color(70, 74, 90);

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

        button.addActionListener(e -> {
            System.out.println("=== CLIC sur: " + panelName + " ===");
            showPanel(panelName);
        });

        return button;
    }

    private static void showPanel(String name) {
        System.out.println("=== showPanel: " + name + " ===");
        if (cardLayout == null || contentPanel == null) {
            System.err.println("ERREUR: cardLayout ou contentPanel est null");
            return;
        }
        cardLayout.show(contentPanel, name);
        contentPanel.revalidate();
        contentPanel.repaint();
        System.out.println("=== Panel affiche: " + name + " ===");
    }

    public static void toggleTheme() {
        try {
            ThemeManager.toggleMode();
            if (ThemeManager.isDarkMode()) {
                UIManager.setLookAndFeel(new FlatOneDarkIJTheme());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(frame);
            frame.revalidate();
            frame.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
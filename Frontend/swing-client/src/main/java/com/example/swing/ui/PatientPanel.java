package com.example.swing.ui;

import com.example.swing.ThemeManager;
import com.example.swing.api.ApiClient;
import com.example.swing.listener.DataChangeManager;
import com.example.swing.model.Patient;
import com.example.swing.ui.dialog.PatientDialog;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Type;
import java.util.List;

public class PatientPanel extends JPanel {

    private final ApiClient apiClient;

    private JTextField champRecherche;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel topPanel;

    private static final String PLACEHOLDER_TEXT = "Rechercher par code ou nom...";
    private List<Patient> allPatients;

    public PatientPanel() {
        this.apiClient = new ApiClient();
        setLayout(new BorderLayout(15, 15));
        setBackground(ThemeManager.getBackgroundColor());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        add(creerTable(), BorderLayout.CENTER);

        chargerListe();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(ThemeManager.getBackgroundColor());
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 5));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Liste des patients");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ThemeManager.getTextColor());

        champRecherche = new JTextField();
        champRecherche.setPreferredSize(new Dimension(250, 35));
        styleTextField(champRecherche);
        setupPlaceholder(champRecherche, PLACEHOLDER_TEXT);

        // ✅ Recherche automatique à chaque frappe
        champRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rechercherAutomatique();
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(champRecherche);

        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.SOUTH);

        JButton btnAjouter = createActionButton("+ Ajouter", new Color(52, 152, 219));
        btnAjouter.addActionListener(e -> ajouter());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAjouter);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void setupPlaceholder(JTextField field, String placeholder) {
        setPlaceholder(field, placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(ThemeManager.getTextColor());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    setPlaceholder(field, placeholder);
                }
            }
        });
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(ThemeManager.getPlaceholderColor());
    }

    private JScrollPane creerTable() {
        tableModel = new DefaultTableModel(
            new Object[]{"Code", "Nom", "Prenom", "Sexe", "Adresse", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        table = new JTable(tableModel);

        table.setBackground(ThemeManager.getTableBackground());
        table.setForeground(ThemeManager.getTableForeground());
        table.setGridColor(ThemeManager.getBorderColor());
        table.setRowHeight(55);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(88, 101, 242));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(5, 5));

        table.getTableHeader().setBackground(ThemeManager.getTableHeaderBackground());
        table.getTableHeader().setForeground(ThemeManager.getTableHeaderForeground());
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(220);

        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true));
        scrollPane.getViewport().setBackground(ThemeManager.getTableBackground());
        scrollPane.setBackground(ThemeManager.getScrollPaneBackground());

        return scrollPane;
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnModifier;
        private JButton btnSupprimer;

        public ButtonRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(true);
            setBackground(ThemeManager.getTableBackground());

            btnModifier = new JButton("Modifier");
            btnModifier.setBackground(new Color(241, 196, 15));
            btnModifier.setForeground(Color.WHITE);
            btnModifier.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnModifier.setFocusPainted(false);
            btnModifier.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            btnModifier.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnSupprimer = new JButton("Supprimer");
            btnSupprimer.setBackground(new Color(231, 76, 60));
            btnSupprimer.setForeground(Color.WHITE);
            btnSupprimer.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnSupprimer.setFocusPainted(false);
            btnSupprimer.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            btnSupprimer.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnModifier.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnModifier.setBackground(new Color(211, 166, 0));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnModifier.setBackground(new Color(241, 196, 15));
                }
            });

            btnSupprimer.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnSupprimer.setBackground(new Color(192, 57, 43));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnSupprimer.setBackground(new Color(231, 76, 60));
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 5, 0, 5);
            add(btnModifier, gbc);

            gbc.gridx = 1;
            add(btnSupprimer, gbc);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(ThemeManager.getTableBackground());
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnModifier;
        private JButton btnSupprimer;
        private int currentRow;

        public ButtonEditor() {
            panel = new JPanel(new GridBagLayout());
            panel.setOpaque(true);
            panel.setBackground(ThemeManager.getTableBackground());

            btnModifier = new JButton("Modifier");
            btnModifier.setBackground(new Color(241, 196, 15));
            btnModifier.setForeground(Color.WHITE);
            btnModifier.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnModifier.setFocusPainted(false);
            btnModifier.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            btnModifier.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnSupprimer = new JButton("Supprimer");
            btnSupprimer.setBackground(new Color(231, 76, 60));
            btnSupprimer.setForeground(Color.WHITE);
            btnSupprimer.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnSupprimer.setFocusPainted(false);
            btnSupprimer.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            btnSupprimer.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnModifier.addActionListener(e -> {
                fireEditingStopped();
                modifier(currentRow);
            });

            btnSupprimer.addActionListener(e -> {
                fireEditingStopped();
                supprimer(currentRow);
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 5, 0, 5);
            panel.add(btnModifier, gbc);

            gbc.gridx = 1;
            panel.add(btnSupprimer, gbc);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.currentRow = row;
            panel.setBackground(ThemeManager.getTableBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private void styleTextField(JTextField field) {
        field.setBackground(ThemeManager.getSurfaceColor());
        field.setForeground(ThemeManager.getTextColor());
        field.setCaretColor(ThemeManager.getTextColor());
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void chargerListe() {
        try {
            String json = apiClient.get("/patients");

            if (json == null || json.isEmpty() || json.equals("[]")) {
                tableModel.setRowCount(0);
                allPatients = null;
                return;
            }

            Type listType = new TypeToken<List<Patient>>() {}.getType();
            allPatients = apiClient.getGson().fromJson(json, listType);

            afficherPatients(allPatients);
        } catch (Exception ex) {
            afficherErreur("Erreur lors du chargement : " + ex.getMessage());
        }
    }

    private void afficherPatients(List<Patient> patients) {
        tableModel.setRowCount(0);
        if (patients != null) {
            for (Patient p : patients) {
                tableModel.addRow(new Object[]{
                    p.getCodepat(),
                    p.getNom(),
                    p.getPrenom(),
                    p.getSexe(),
                    p.getAdresse(),
                    "Actions"
                });
            }
        }
    }

    // ✅ Recherche et réinitialisation automatiques
    private void rechercherAutomatique() {
        String terme = champRecherche.getText().trim();

        // ✅ Si le champ est vide ou contient le placeholder → réinitialisation automatique
        if (terme.isEmpty() || terme.equals(PLACEHOLDER_TEXT)) {
            chargerListe(); // ← Réinitialisation automatique
            return;
        }

        if (allPatients == null) {
            chargerListe();
            if (allPatients == null) {
                return;
            }
        }

        // Recherche en temps réel (contient)
        List<Patient> resultats = allPatients.stream()
            .filter(p -> {
                String code = p.getCodepat().toLowerCase();
                String nom = p.getNom().toLowerCase();
                String prenom = p.getPrenom().toLowerCase();
                String recherche = terme.toLowerCase();
                return code.contains(recherche) ||
                       nom.contains(recherche) ||
                       prenom.contains(recherche);
            })
            .collect(java.util.stream.Collectors.toList());

        // Afficher les résultats (ou vide si aucun)
        tableModel.setRowCount(0);
        if (!resultats.isEmpty()) {
            afficherPatients(resultats);
        }
        // Si aucun résultat, le tableau reste vide
    }

    private void ajouter() {
        PatientDialog dialog = new PatientDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Ajouter un patient",
            null
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Patient patient = dialog.getResult();
            try {
                apiClient.post("/patients", patient);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Patient ajoute avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de l'ajout : " + ex.getMessage());
            }
        }
    }

    private void modifier(int row) {
        String code = tableModel.getValueAt(row, 0).toString();
        String nom = tableModel.getValueAt(row, 1).toString();
        String prenom = tableModel.getValueAt(row, 2).toString();
        Character sexe = tableModel.getValueAt(row, 3).toString().charAt(0);
        String adresse = tableModel.getValueAt(row, 4).toString();

        Patient patientExist = new Patient(code, nom, prenom, sexe, adresse);

        PatientDialog dialog = new PatientDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Modifier un patient",
            patientExist
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Patient patient = dialog.getResult();
            try {
                apiClient.put("/patients/" + patient.getCodepat(), patient);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Patient modifie avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de la modification : " + ex.getMessage());
            }
        }
    }

    private void supprimer(int row) {
        String code = tableModel.getValueAt(row, 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(this,
            "Supprimer le patient " + code + " ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                apiClient.delete("/patients/" + code);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Patient supprime avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de la suppression : " + ex.getMessage());
            }
        }
    }

    public void updateTheme() {
        setBackground(ThemeManager.getBackgroundColor());

        if (topPanel != null) {
            topPanel.setBackground(ThemeManager.getBackgroundColor());
            updatePanelRecursively(topPanel);
            topPanel.repaint();
        }

        if (champRecherche != null) {
            champRecherche.setBackground(ThemeManager.getSurfaceColor());
            champRecherche.setForeground(ThemeManager.getTextColor());
            champRecherche.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }

        if (table != null) {
            table.setBackground(ThemeManager.getTableBackground());
            table.setForeground(ThemeManager.getTableForeground());
            table.setGridColor(ThemeManager.getBorderColor());
            table.getTableHeader().setBackground(ThemeManager.getTableHeaderBackground());
            table.getTableHeader().setForeground(ThemeManager.getTableHeaderForeground());

            if (table.getParent() instanceof JViewport) {
                table.getParent().setBackground(ThemeManager.getTableBackground());
            }

            if (table.getParent().getParent() instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
                scrollPane.setBorder(BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true));
                scrollPane.getViewport().setBackground(ThemeManager.getTableBackground());
                scrollPane.setBackground(ThemeManager.getScrollPaneBackground());
            }

            table.repaint();
        }

        revalidate();
        repaint();
    }

    private void updatePanelRecursively(JPanel parent) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel p = (JPanel) comp;
                if (p.isOpaque()) {
                    p.setBackground(ThemeManager.getBackgroundColor());
                }
                updatePanelRecursively(p);
            } else if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(ThemeManager.getTextColor());
            }
            comp.repaint();
        }
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Succes", JOptionPane.INFORMATION_MESSAGE);
    }
}
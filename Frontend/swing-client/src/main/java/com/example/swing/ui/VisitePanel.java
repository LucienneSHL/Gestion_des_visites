package com.example.swing.ui;

import com.example.swing.ThemeManager;
import com.example.swing.api.ApiClient;
import com.example.swing.listener.DataChangeListener;
import com.example.swing.listener.DataChangeManager;
import com.example.swing.model.Medecin;
import com.example.swing.model.Patient;
import com.example.swing.model.Visite;
import com.example.swing.model.VisiteId;
import com.example.swing.ui.dialog.VisiteDialog;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.List;

public class VisitePanel extends JPanel implements DataChangeListener {

    private final ApiClient apiClient;

    private JTable table;
    private DefaultTableModel tableModel;
    private List<Visite> allVisites;

    public VisitePanel() {
        this.apiClient = new ApiClient();
        setLayout(new BorderLayout(15, 15));
        setBackground(ThemeManager.getBackgroundColor());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        add(creerTable(), BorderLayout.CENTER);

        DataChangeManager.getInstance().addListener(this);

        chargerListe();
    }

    @Override
    public void onDataChanged() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("VisitePanel: Mise a jour automatique...");
            chargerListe();
        });
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeManager.getBackgroundColor());
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Liste des visites");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ThemeManager.getTextColor());

        JButton btnAjouter = createActionButton("+ Ajouter une visite", new Color(52, 152, 219));
        btnAjouter.addActionListener(e -> ajouter());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAjouter);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane creerTable() {
        tableModel = new DefaultTableModel(
            new Object[]{"Medecin", "Patient", "Date", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
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

        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(240);

        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());

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
            // ✅ Recalcule la couleur a chaque affichage pour suivre le theme actuel
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
            // ✅ Recalcule la couleur a chaque affichage pour suivre le theme actuel
            panel.setBackground(ThemeManager.getTableBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
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

    public void chargerListe() {
        try {
            System.out.println("Chargement des visites...");
            String json = apiClient.get("/visites");

            if (json == null || json.isEmpty() || json.equals("[]")) {
                tableModel.setRowCount(0);
                allVisites = null;
                return;
            }

            Type listType = new TypeToken<List<Visite>>() {}.getType();
            allVisites = apiClient.getGson().fromJson(json, listType);

            tableModel.setRowCount(0);
            if (allVisites != null) {
                for (Visite v : allVisites) {
                    String nomMedecin = v.getMedecin() != null ?
                        v.getMedecin().toString() : v.getId().getCodemed();
                    String nomPatient = v.getPatient() != null ?
                        v.getPatient().toString() : v.getId().getCodepat();
                    tableModel.addRow(new Object[]{
                        nomMedecin,
                        nomPatient,
                        v.getId().getDate(),
                        "Actions"
                    });
                }
                System.out.println("Charge " + allVisites.size() + " visites");
            }
        } catch (Exception ex) {
            System.err.println("Erreur lors du chargement: " + ex.getMessage());
            afficherErreur("Erreur lors du chargement : " + ex.getMessage());
        }
    }

    private List<Medecin> getMedecins() {
        try {
            String json = apiClient.get("/medecins");
            Type listType = new TypeToken<List<Medecin>>() {}.getType();
            return apiClient.getGson().fromJson(json, listType);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Patient> getPatients() {
        try {
            String json = apiClient.get("/patients");
            Type listType = new TypeToken<List<Patient>>() {}.getType();
            return apiClient.getGson().fromJson(json, listType);
        } catch (Exception e) {
            return null;
        }
    }

    private void ajouter() {
        List<Medecin> medecins = getMedecins();
        List<Patient> patients = getPatients();

        if (medecins == null || medecins.isEmpty() || patients == null || patients.isEmpty()) {
            afficherErreur("Aucun medecin ou patient disponible. Creez-en d'abord.");
            return;
        }

        VisiteDialog dialog = new VisiteDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Ajouter une visite",
            medecins,
            patients,
            null
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Visite visite = dialog.getResult();
            try {
                apiClient.post("/visites", visite);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Visite ajoutee avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de l'ajout : " + ex.getMessage());
            }
        }
    }

    private void modifier(int row) {
        if (allVisites == null || row >= allVisites.size()) {
            afficherErreur("Impossible de trouver la visite selectionnee.");
            return;
        }

        Visite visiteExist = allVisites.get(row);

        List<Medecin> medecins = getMedecins();
        List<Patient> patients = getPatients();

        if (medecins == null || medecins.isEmpty() || patients == null || patients.isEmpty()) {
            afficherErreur("Aucun medecin ou patient disponible.");
            return;
        }

        VisiteDialog dialog = new VisiteDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Modifier une visite",
            medecins,
            patients,
            visiteExist
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Visite visite = dialog.getResult();
            try {
                String endpoint = "/visites/" + visiteExist.getId().getCodemed()
                        + "/" + visiteExist.getId().getCodepat()
                        + "/" + visiteExist.getId().getDate();
                apiClient.put(endpoint, visite);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Visite modifiee avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de la modification : " + ex.getMessage());
            }
        }
    }

    private void supprimer(int row) {
        if (allVisites == null || row >= allVisites.size()) {
            afficherErreur("Impossible de trouver la visite selectionnee.");
            return;
        }

        Visite visite = allVisites.get(row);
        String info = visite.getMedecin() != null ?
            visite.getMedecin().getNom() : visite.getId().getCodemed();

        int confirmation = JOptionPane.showConfirmDialog(this,
            "Supprimer la visite du medecin " + info + " ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmation != JOptionPane.YES_OPTION) return;

        try {
            String endpoint = "/visites/" + visite.getId().getCodemed()
                    + "/" + visite.getId().getCodepat()
                    + "/" + visite.getId().getDate();

            apiClient.delete(endpoint);
            chargerListe();
            DataChangeManager.getInstance().notifyDataChanged();
            showSuccess("Visite supprimee avec succes.");
        } catch (Exception ex) {
            afficherErreur("Erreur lors de la suppression : " + ex.getMessage());
        }
    }

    public void updateTheme() {
        setBackground(ThemeManager.getBackgroundColor());

        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(ThemeManager.getBackgroundColor());
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JLabel) {
                        ((JLabel) subComp).setForeground(ThemeManager.getTextColor());
                    }
                }
            }
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

            // ✅ Force le rafraichissement des cellules de boutons (colonne Actions)
            table.repaint();
        }

        revalidate();
        repaint();
    }

    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Succes", JOptionPane.INFORMATION_MESSAGE);
    }
}
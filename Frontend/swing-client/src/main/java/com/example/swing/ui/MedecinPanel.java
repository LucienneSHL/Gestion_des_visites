package com.example.swing.ui;

import com.example.swing.ThemeManager;
import com.example.swing.api.ApiClient;
import com.example.swing.listener.DataChangeManager;
import com.example.swing.model.Medecin;
import com.example.swing.ui.dialog.MedecinDialog;
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

public class MedecinPanel extends JPanel {

    private final ApiClient apiClient;

    private JTable table;
    private DefaultTableModel tableModel;

    public MedecinPanel() {
        this.apiClient = new ApiClient();
        setLayout(new BorderLayout(15, 15));
        setBackground(ThemeManager.getBackgroundColor());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        add(creerTable(), BorderLayout.CENTER);

        chargerListe();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeManager.getBackgroundColor());
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Liste des medecins");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ThemeManager.getTextColor());

        JButton btnAjouter = createActionButton("+ Ajouter", new Color(52, 152, 219));
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
            new Object[]{"Code", "Nom", "Prenom", "Grade", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
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
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(220);

        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor());

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
            String json = apiClient.get("/medecins");

            if (json == null || json.isEmpty() || json.equals("[]")) {
                tableModel.setRowCount(0);
                return;
            }

            Type listType = new TypeToken<List<Medecin>>() {}.getType();
            List<Medecin> medecins = apiClient.getGson().fromJson(json, listType);

            tableModel.setRowCount(0);
            if (medecins != null) {
                for (Medecin m : medecins) {
                    tableModel.addRow(new Object[]{
                        m.getCodemed(),
                        m.getNom(),
                        m.getPrenom(),
                        m.getGrade(),
                        "Actions"
                    });
                }
            }
        } catch (Exception ex) {
            afficherErreur("Erreur lors du chargement : " + ex.getMessage());
        }
    }

    private void ajouter() {
        MedecinDialog dialog = new MedecinDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Ajouter un medecin",
            null
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Medecin medecin = dialog.getResult();
            try {
                apiClient.post("/medecins", medecin);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Medecin ajoute avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de l'ajout : " + ex.getMessage());
            }
        }
    }

    private void modifier(int row) {
        String code = tableModel.getValueAt(row, 0).toString();
        String nom = tableModel.getValueAt(row, 1).toString();
        String prenom = tableModel.getValueAt(row, 2).toString();
        String grade = tableModel.getValueAt(row, 3).toString();

        Medecin medecinExist = new Medecin(code, nom, prenom, grade);

        MedecinDialog dialog = new MedecinDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Modifier un medecin",
            medecinExist
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Medecin medecin = dialog.getResult();
            try {
                apiClient.put("/medecins/" + medecin.getCodemed(), medecin);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Medecin modifie avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de la modification : " + ex.getMessage());
            }
        }
    }

    private void supprimer(int row) {
        String code = tableModel.getValueAt(row, 0).toString();

        int confirmation = JOptionPane.showConfirmDialog(this,
            "Supprimer le medecin " + code + " ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                apiClient.delete("/medecins/" + code);
                chargerListe();
                DataChangeManager.getInstance().notifyDataChanged();
                showSuccess("Medecin supprime avec succes.");
            } catch (Exception ex) {
                afficherErreur("Erreur lors de la suppression : " + ex.getMessage());
            }
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
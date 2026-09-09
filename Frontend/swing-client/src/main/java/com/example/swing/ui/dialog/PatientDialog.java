package com.example.swing.ui.dialog;

import com.example.swing.ThemeManager;
import com.example.swing.model.Patient;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PatientDialog extends JDialog {
    
    private JTextField champCode, champNom, champPrenom, champAdresse;
    private JComboBox<String> comboSexe;
    private boolean confirmed = false;
    private Patient result;
    
    public PatientDialog(JFrame parent, String title, Patient patientToEdit) {
        super(parent, title, true);
        setSize(600, 430);
        setLocationRelativeTo(parent);
        setBackground(ThemeManager.getDialogBackground());
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ThemeManager.getDialogBackground());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header avec icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        FontIcon patientIcon = FontIcon.of(FontAwesomeSolid.USER, 20, new Color(88, 101, 242));
        JLabel iconLabel = new JLabel(patientIcon);
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        headerPanel.add(iconLabel, BorderLayout.WEST);
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(ThemeManager.getDialogTextColor());
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        // Formulaire avec GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ThemeManager.getDialogBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        champCode = createTextField();
        champNom = createTextField();
        champPrenom = createTextField();
        champAdresse = createTextField();
        comboSexe = new JComboBox<>(new String[]{"M", "F"});
        styleComboBox(comboSexe);
        
        if (patientToEdit != null) {
            champCode.setText(patientToEdit.getCodepat());
            champNom.setText(patientToEdit.getNom());
            champPrenom.setText(patientToEdit.getPrenom());
            comboSexe.setSelectedItem(String.valueOf(patientToEdit.getSexe()));
            champAdresse.setText(patientToEdit.getAdresse());
            champCode.setEnabled(false);
        }
        
        // Ligne 1: Code
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Code :"), gbc);
        gbc.gridx = 1;
        formPanel.add(champCode, gbc);
        
        // Ligne 2: Nom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Nom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(champNom, gbc);
        
        // Ligne 3: Prénom
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Prenom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(champPrenom, gbc);
        
        // Ligne 4: Sexe
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Sexe :"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboSexe, gbc);
        
        // Ligne 5: Adresse
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Adresse :"), gbc);
        gbc.gridx = 1;
        formPanel.add(champAdresse, gbc);
        
        // Boutons avec icônes
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(ThemeManager.getDialogBackground());
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton btnOk = createButton("Valider", new Color(52, 152, 219), FontAwesomeSolid.CHECK);
        JButton btnCancel = createButton("Annuler", new Color(231, 76, 60), FontAwesomeSolid.TIMES);
        
        btnOk.addActionListener(e -> {
            if (validerFormulaire()) {
                confirmed = true;
                result = new Patient(
                    champCode.getText().trim(),
                    champNom.getText().trim(),
                    champPrenom.getText().trim(),
                    comboSexe.getSelectedItem().toString().charAt(0),
                    champAdresse.getText().trim()
                );
                dispose();
            }
        });
        
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(ThemeManager.getDialogFieldBackground());
        field.setForeground(ThemeManager.getDialogTextColor());
        field.setCaretColor(ThemeManager.getDialogTextColor());
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ThemeManager.getDialogTextColor());
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(ThemeManager.getComboBoxBackground());
        comboBox.setForeground(ThemeManager.getDialogTextColor());
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private JButton createButton(String text, Color bgColor, FontAwesomeSolid icon) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        FontIcon buttonIcon = FontIcon.of(icon, 14, Color.WHITE);
        button.setIcon(buttonIcon);
        button.setIconTextGap(8);
        
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
    
    private boolean validerFormulaire() {
        if (champCode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le code est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            champCode.requestFocus();
            return false;
        }
        if (champNom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            champNom.requestFocus();
            return false;
        }
        if (champPrenom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le prenom est obligatoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            champPrenom.requestFocus();
            return false;
        }
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Patient getResult() {
        return result;
    }
}
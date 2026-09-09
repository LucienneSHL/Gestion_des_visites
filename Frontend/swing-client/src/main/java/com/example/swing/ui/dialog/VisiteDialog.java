package com.example.swing.ui.dialog;

import com.example.swing.ThemeManager;
import com.example.swing.model.Medecin;
import com.example.swing.model.Patient;
import com.example.swing.model.Visite;
import com.example.swing.model.VisiteId;
import com.example.swing.ui.component.CustomDatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VisiteDialog extends JDialog {
    
    private JComboBox<Medecin> comboMedecin;
    private JComboBox<Patient> comboPatient;
    private CustomDatePicker datePicker;
    private boolean confirmed = false;
    private Visite result;
    
    private boolean isEditMode = false;
    
    public VisiteDialog(JFrame parent, String title, 
                        List<Medecin> medecins, 
                        List<Patient> patients,
                        Visite visiteToEdit) {
        super(parent, title, true);
        setSize(600, 420);
        setLocationRelativeTo(parent);
        setBackground(ThemeManager.getDialogBackground());
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(ThemeManager.getDialogBackground());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header sans icône
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(ThemeManager.getDialogTextColor());
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        // Formulaire avec GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(ThemeManager.getDialogBackground());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        comboMedecin = new JComboBox<>();
        comboPatient = new JComboBox<>();
        datePicker = new CustomDatePicker();
        
        styleComboBox(comboMedecin);
        styleComboBox(comboPatient);
        
        if (medecins != null) {
            for (Medecin m : medecins) {
                comboMedecin.addItem(m);
            }
        }
        
        if (patients != null) {
            for (Patient p : patients) {
                comboPatient.addItem(p);
            }
        }
        
        isEditMode = (visiteToEdit != null);
        if (isEditMode) {
            for (int i = 0; i < comboMedecin.getItemCount(); i++) {
                Medecin m = comboMedecin.getItemAt(i);
                if (m.getCodemed().equals(visiteToEdit.getId().getCodemed())) {
                    comboMedecin.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < comboPatient.getItemCount(); i++) {
                Patient p = comboPatient.getItemAt(i);
                if (p.getCodepat().equals(visiteToEdit.getId().getCodepat())) {
                    comboPatient.setSelectedIndex(i);
                    break;
                }
            }
            
            datePicker.setDate(visiteToEdit.getId().getDate());
        }
        
        // Ligne 1: Médecin
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Medecin :"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboMedecin, gbc);
        
        // Ligne 2: Patient
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Patient :"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboPatient, gbc);
        
        // Ligne 3: Date
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Date :"), gbc);
        gbc.gridx = 1;
        formPanel.add(datePicker, gbc);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(ThemeManager.getDialogBackground());
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton btnOk = createButton("Valider", new Color(52, 152, 219));
        JButton btnCancel = createButton("Annuler", new Color(231, 76, 60));
        
        btnOk.addActionListener(e -> {
            if (validerFormulaire()) {
                confirmed = true;
                Medecin medecin = (Medecin) comboMedecin.getSelectedItem();
                Patient patient = (Patient) comboPatient.getSelectedItem();
                String date = datePicker.getDate();
                
                VisiteId id = new VisiteId(medecin.getCodemed(), patient.getCodepat(), date);
                result = new Visite(id, medecin, patient);
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
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ThemeManager.getDialogTextColor());
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return label;
    }
    
    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(ThemeManager.getComboBoxBackground());
        comboBox.setForeground(ThemeManager.getDialogTextColor());
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
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
    
    private boolean validerFormulaire() {
        if (comboMedecin.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selectionnez un medecin.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (comboPatient.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selectionnez un patient.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (datePicker.getDate().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selectionnez une date.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Visite getResult() {
        return result;
    }
}
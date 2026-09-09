package com.example.swing.ui.component;

import com.example.swing.ThemeManager;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDatePicker extends JPanel {
    
    private JDateChooser dateChooser;
    
    public CustomDatePicker() {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.getDialogBackground());
        
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setBackground(ThemeManager.getDialogFieldBackground());
        dateChooser.setForeground(ThemeManager.getDialogTextColor());
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Style du champ de texte
        if (dateChooser.getDateEditor() != null) {
            JTextField textField = (JTextField) dateChooser.getDateEditor().getUiComponent();
            if (textField != null) {
                textField.setBackground(ThemeManager.getDialogFieldBackground());
                textField.setForeground(ThemeManager.getDialogTextColor());
                textField.setCaretColor(ThemeManager.getDialogTextColor());
                textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            }
        }
        
        // Bordure personnalisée
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        dateChooser.setEnabled(true);
        
        add(dateChooser, BorderLayout.CENTER);
    }
    
    /**
     * Récupère la date au format yyyy-MM-dd
     */
    public String getDate() {
        Date date = dateChooser.getDate();
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    /**
     * Définit la date à partir d'une chaîne au format yyyy-MM-dd
     */
    public void setDate(String date) {
        if (date == null || date.isEmpty()) {
            dateChooser.setDate(null);
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            dateChooser.setDate(d);
        } catch (Exception e) {
            dateChooser.setDate(null);
        }
    }
    
    /**
     * Définit la date du jour
     */
    public void setDateToday() {
        dateChooser.setDate(new Date());
    }
    
    /**
     * Efface la date
     */
    public void clear() {
        dateChooser.setDate(null);
    }
    
    /**
     * Active ou désactive le composant
     */
    public void setEnabled(boolean enabled) {
        dateChooser.setEnabled(enabled);
        if (dateChooser.getDateEditor() != null) {
            JTextField textField = (JTextField) dateChooser.getDateEditor().getUiComponent();
            if (textField != null) {
                textField.setEnabled(enabled);
            }
        }
    }
    
    /**
     * Vérifie si une date est sélectionnée
     */
    public boolean hasDate() {
        return dateChooser.getDate() != null;
    }
    
    /**
     * Met à jour le thème du DatePicker
     */
    public void updateTheme() {
        setBackground(ThemeManager.getDialogBackground());
        dateChooser.setBackground(ThemeManager.getDialogFieldBackground());
        dateChooser.setForeground(ThemeManager.getDialogTextColor());
        
        if (dateChooser.getDateEditor() != null) {
            JTextField textField = (JTextField) dateChooser.getDateEditor().getUiComponent();
            if (textField != null) {
                textField.setBackground(ThemeManager.getDialogFieldBackground());
                textField.setForeground(ThemeManager.getDialogTextColor());
                textField.setCaretColor(ThemeManager.getDialogTextColor());
            }
        }
        
        dateChooser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.getBorderColor(), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        revalidate();
        repaint();
    }
}
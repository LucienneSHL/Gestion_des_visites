package com.example.swing;

import java.awt.Color;

public class ThemeManager {
    
    private static boolean isDarkMode = true;
    
    // Couleurs pour le mode sombre
    private static final Color DARK_BACKGROUND = new Color(44, 48, 60);
    private static final Color DARK_SURFACE = new Color(35, 38, 50);
    private static final Color DARK_TEXT = Color.WHITE;
    private static final Color DARK_HEADER = new Color(88, 101, 242);
    private static final Color DARK_TABLE_BG = new Color(35, 38, 50);
    private static final Color DARK_TABLE_FG = Color.WHITE;
    private static final Color DARK_TABLE_HEADER_BG = new Color(50, 54, 67);
    private static final Color DARK_TABLE_HEADER_FG = new Color(200, 200, 220);
    private static final Color DARK_BORDER = new Color(50, 54, 67);
    private static final Color DARK_PLACEHOLDER = new Color(128, 128, 128);
    private static final Color DARK_MENU = new Color(150, 150, 180);
    private static final Color DARK_SEPARATOR = new Color(70, 74, 90);
    private static final Color DARK_SIDEBAR_BG = new Color(44, 48, 60);
    private static final Color DARK_SCROLLPANE_BG = new Color(35, 38, 50);
    private static final Color DARK_BUTTON_BG = new Color(50, 54, 67);
    private static final Color DARK_BUTTON_FG = new Color(220, 220, 230);
    private static final Color DARK_BUTTON_HOVER = new Color(70, 74, 90);
    private static final Color DARK_DIALOG_BG = new Color(44, 48, 60);
    private static final Color DARK_DIALOG_TEXT = Color.WHITE;
    private static final Color DARK_DIALOG_FIELD_BG = new Color(35, 38, 50);
    private static final Color DARK_COMBO_BG = new Color(35, 38, 50);
    
    // Couleurs pour le mode clair
    private static final Color LIGHT_BACKGROUND = new Color(240, 242, 245);
    private static final Color LIGHT_SURFACE = Color.WHITE;
    private static final Color LIGHT_TEXT = new Color(30, 34, 45);
    private static final Color LIGHT_HEADER = new Color(88, 101, 242);
    private static final Color LIGHT_TABLE_BG = Color.WHITE;
    private static final Color LIGHT_TABLE_FG = new Color(30, 34, 45);
    private static final Color LIGHT_TABLE_HEADER_BG = new Color(220, 222, 225);
    private static final Color LIGHT_TABLE_HEADER_FG = new Color(30, 34, 45);
    private static final Color LIGHT_BORDER = new Color(200, 200, 210);
    private static final Color LIGHT_PLACEHOLDER = new Color(150, 150, 150);
    private static final Color LIGHT_MENU = new Color(80, 80, 100);
    private static final Color LIGHT_SEPARATOR = new Color(200, 200, 210);
    private static final Color LIGHT_SIDEBAR_BG = Color.WHITE;
    private static final Color LIGHT_SCROLLPANE_BG = Color.WHITE;
    private static final Color LIGHT_BUTTON_BG = new Color(240, 242, 245);
    private static final Color LIGHT_BUTTON_FG = new Color(30, 34, 45);
    private static final Color LIGHT_BUTTON_HOVER = new Color(220, 222, 225);
    private static final Color LIGHT_DIALOG_BG = Color.WHITE;
    private static final Color LIGHT_DIALOG_TEXT = new Color(30, 34, 45);
    private static final Color LIGHT_DIALOG_FIELD_BG = Color.WHITE;
    private static final Color LIGHT_COMBO_BG = Color.WHITE;
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    public static void setDarkMode(boolean dark) {
        isDarkMode = dark;
    }
    
    public static void toggleMode() {
        isDarkMode = !isDarkMode;
    }
    
    public static Color getBackgroundColor() {
        return isDarkMode ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }
    
    public static Color getSurfaceColor() {
        return isDarkMode ? DARK_SURFACE : LIGHT_SURFACE;
    }
    
    public static Color getTextColor() {
        return isDarkMode ? DARK_TEXT : LIGHT_TEXT;
    }
    
    public static Color getHeaderColor() {
        return isDarkMode ? DARK_HEADER : LIGHT_HEADER;
    }
    
    public static Color getTableBackground() {
        return isDarkMode ? DARK_TABLE_BG : LIGHT_TABLE_BG;
    }
    
    public static Color getTableForeground() {
        return isDarkMode ? DARK_TABLE_FG : LIGHT_TABLE_FG;
    }
    
    public static Color getTableHeaderBackground() {
        return isDarkMode ? DARK_TABLE_HEADER_BG : LIGHT_TABLE_HEADER_BG;
    }
    
    public static Color getTableHeaderForeground() {
        return isDarkMode ? DARK_TABLE_HEADER_FG : LIGHT_TABLE_HEADER_FG;
    }
    
    public static Color getBorderColor() {
        return isDarkMode ? DARK_BORDER : LIGHT_BORDER;
    }
    
    public static Color getPlaceholderColor() {
        return isDarkMode ? DARK_PLACEHOLDER : LIGHT_PLACEHOLDER;
    }
    
    public static Color getMenuColor() {
        return isDarkMode ? DARK_MENU : LIGHT_MENU;
    }
    
    public static Color getSeparatorColor() {
        return isDarkMode ? DARK_SEPARATOR : LIGHT_SEPARATOR;
    }
    
    public static Color getSidebarBackground() {
        return isDarkMode ? DARK_SIDEBAR_BG : LIGHT_SIDEBAR_BG;
    }
    
    public static Color getScrollPaneBackground() {
        return isDarkMode ? DARK_SCROLLPANE_BG : LIGHT_SCROLLPANE_BG;
    }
    
    public static Color getButtonBackground() {
        return isDarkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG;
    }
    
    public static Color getButtonForeground() {
        return isDarkMode ? DARK_BUTTON_FG : LIGHT_BUTTON_FG;
    }
    
    public static Color getButtonHoverColor() {
        return isDarkMode ? DARK_BUTTON_HOVER : LIGHT_BUTTON_HOVER;
    }
    
    public static Color getDialogBackground() {
        return isDarkMode ? DARK_DIALOG_BG : LIGHT_DIALOG_BG;
    }
    
    public static Color getDialogTextColor() {
        return isDarkMode ? DARK_DIALOG_TEXT : LIGHT_DIALOG_TEXT;
    }
    
    public static Color getDialogFieldBackground() {
        return isDarkMode ? DARK_DIALOG_FIELD_BG : LIGHT_DIALOG_FIELD_BG;
    }
    
    public static Color getComboBoxBackground() {
        return isDarkMode ? DARK_COMBO_BG : LIGHT_COMBO_BG;
    }
}
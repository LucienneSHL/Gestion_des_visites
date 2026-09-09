package com.example.swing.model;

public class VisiteId {
    private String codemed;
    private String codepat;
    private String date; // format "yyyy-MM-dd", pour correspondre à LocalDate côté backend

    public VisiteId() {
    }

    public VisiteId(String codemed, String codepat, String date) {
        this.codemed = codemed;
        this.codepat = codepat;
        this.date = date;
    }

    public String getCodemed() { return codemed; }
    public void setCodemed(String codemed) { this.codemed = codemed; }

    public String getCodepat() { return codepat; }
    public void setCodepat(String codepat) { this.codepat = codepat; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
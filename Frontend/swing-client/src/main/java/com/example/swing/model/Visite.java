package com.example.swing.model;

public class Visite {
    private VisiteId id;
    private Medecin medecin;
    private Patient patient;

    public Visite() {
    }

    public Visite(VisiteId id, Medecin medecin, Patient patient) {
        this.id = id;
        this.medecin = medecin;
        this.patient = patient;
    }

    public VisiteId getId() { return id; }
    public void setId(VisiteId id) { this.id = id; }

    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}
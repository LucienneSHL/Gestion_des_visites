package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getById(String codepat) {
        return patientRepository.findById(codepat)
                .orElseThrow(() -> new RuntimeException("Patient introuvable avec le code : " + codepat));
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient update(String codepat, Patient patientDetails) {
        Patient patient = getById(codepat);
        patient.setNom(patientDetails.getNom());
        patient.setPrenom(patientDetails.getPrenom());
        patient.setSexe(patientDetails.getSexe());
        patient.setAdresse(patientDetails.getAdresse());
        return patientRepository.save(patient);
    }

    public void delete(String codepat) {
        patientRepository.deleteById(codepat);
    }

    // Recherche par code (exact) ou par nom (contient)
    public List<Patient> searchByNom(String nom) {
        return patientRepository.findByNomContainingIgnoreCase(nom);
    }
}
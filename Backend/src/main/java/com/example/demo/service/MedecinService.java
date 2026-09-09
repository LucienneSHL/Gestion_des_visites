package com.example.demo.service;

import com.example.demo.entity.Medecin;
import com.example.demo.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    public List<Medecin> getAll() {
        return medecinRepository.findAll();
    }

    public Medecin getById(String codemed) {
        return medecinRepository.findById(codemed)
                .orElseThrow(() -> new RuntimeException("Médecin introuvable avec le code : " + codemed));
    }

    public Medecin save(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    public Medecin update(String codemed, Medecin medecinDetails) {
        Medecin medecin = getById(codemed);
        medecin.setNom(medecinDetails.getNom());
        medecin.setPrenom(medecinDetails.getPrenom());
        medecin.setGrade(medecinDetails.getGrade());
        return medecinRepository.save(medecin);
    }

    public void delete(String codemed) {
        medecinRepository.deleteById(codemed);
    }
}
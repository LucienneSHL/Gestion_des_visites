package com.example.demo.service;

import com.example.demo.entity.Visite;
import com.example.demo.entity.VisiteId;
import com.example.demo.repository.VisiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VisiteService {

    @Autowired
    private VisiteRepository visiteRepository;

    public List<Visite> getAll() {
        return visiteRepository.findAll();
    }

    public Visite getById(VisiteId id) {
        return visiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visite introuvable"));
    }

    public Visite save(Visite visite) {
        return visiteRepository.save(visite);
    }

    public void delete(VisiteId id) {
        visiteRepository.deleteById(id);
    }

    // ✅ Nouvelle méthode pour la mise à jour
    @Transactional
    public Visite update(VisiteId oldId, Visite newVisite) {
        // Supprimer l'ancienne
        delete(oldId);
        // Créer la nouvelle
        return save(newVisite);
    }
}
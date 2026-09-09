package com.example.demo.controller;

import com.example.demo.entity.Patient;
import com.example.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public List<Patient> getAll() {
        return patientService.getAll();
    }

    @GetMapping("/{codepat}")
    public Patient getById(@PathVariable String codepat) {
        return patientService.getById(codepat);
    }

    @PostMapping
    public Patient create(@RequestBody Patient patient) {
        return patientService.save(patient);
    }

    @PutMapping("/{codepat}")
    public Patient update(@PathVariable String codepat, @RequestBody Patient patient) {
        return patientService.update(codepat, patient);
    }

    @DeleteMapping("/{codepat}")
    public void delete(@PathVariable String codepat) {
        patientService.delete(codepat);
    }

    // Recherche par nom : GET /api/patients/search?nom=xxx
    @GetMapping("/search")
    public List<Patient> searchByNom(@RequestParam String nom) {
        return patientService.searchByNom(nom);
    }
}
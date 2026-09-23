package com.example.demo.controller;

import com.example.demo.entity.Medecin;
import com.example.demo.service.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medecins")
@CrossOrigin(origins = "*")
public class MedecinController {

    @Autowired
    private MedecinService medecinService;

    @GetMapping
    public List<Medecin> getAll() {
        return medecinService.getAll();
    }

    @GetMapping("/{codemed}")
    public Medecin getById(@PathVariable String codemed) {
        return medecinService.getById(codemed);
    }

    @PostMapping
    public Medecin create(@RequestBody Medecin medecin) {
        return medecinService.save(medecin);
    }

    @PutMapping("/{codemed}")
    public Medecin update(@PathVariable String codemed, @RequestBody Medecin medecin) {
        return medecinService.update(codemed, medecin);
    }

    @DeleteMapping("/{codemed}")
    public void delete(@PathVariable String codemed) {
        medecinService.delete(codemed);
    }
}
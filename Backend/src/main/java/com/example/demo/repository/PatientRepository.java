package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    // Recherche par nom (contient, insensible à la casse)
    List<Patient> findByNomContainingIgnoreCase(String nom);
}
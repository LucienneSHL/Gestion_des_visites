package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @Column(name = "codepat", length = 10)
    private String codepat;   // ex: "PAT001"

    @NotBlank
    @Column(nullable = false, length = 50)
    private String nom;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(length = 1)
    private Character sexe;   // 'M' ou 'F' -> un seul caractère

    @Column(length = 100)
    private String adresse;
}
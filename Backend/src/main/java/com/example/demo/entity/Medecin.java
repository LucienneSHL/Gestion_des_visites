package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medecin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medecin {

    @Id
    @Column(name = "codemed", length = 10)
    private String codemed;   
    @NotBlank
    @Column(nullable = false, length = 50)
    private String nom;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(length = 30)
    private String grade;     // ex: "Généraliste", "Spécialiste", "Chef de service"
}
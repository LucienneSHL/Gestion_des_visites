package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisiteId implements Serializable {

    @Column(name = "codemed", length = 10)
    private String codemed;

    @Column(name = "codepat", length = 10)
    private String codepat;

    @Column(name = "date_visite")
    private LocalDate date;   // LocalDate = type Java moderne pour les dates (sans heure)
}
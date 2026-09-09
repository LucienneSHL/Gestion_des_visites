package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "visite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visite {

    @EmbeddedId
    private VisiteId id;

    @ManyToOne
    @MapsId("codemed")
    @JoinColumn(name = "codemed")
    private Medecin medecin;

    @ManyToOne
    @MapsId("codepat")
    @JoinColumn(name = "codepat")
    private Patient patient;
}
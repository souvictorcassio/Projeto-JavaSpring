package com.backend.aula09.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "adoptions")
@Data
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne // Muitos registros de adoção podem se referir ao mesmo usuário
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne // Muitos registros de adoção podem se referir ao mesmo animal
    @JoinColumn(name = "animal_id")
    @JsonBackReference
    private Animal animal;

    private LocalDateTime adoptionDate;

    public Adoption() {
        this.adoptionDate = LocalDateTime.now(); // Define a data automaticamente
    }
}

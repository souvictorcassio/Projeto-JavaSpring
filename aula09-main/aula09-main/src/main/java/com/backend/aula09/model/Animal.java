package com.backend.aula09.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

import lombok.Data;

import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "animals")
@Data
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nameAnimal;

    private String nameOwner;

    private String emailOwner;

    private String phoneOwner;

    private String animalTypo;

    private String animalBreed;

    private String address;

    private String neighborhood;

    private String city;

    private String state;

    private boolean available = true;

    @OneToMany(mappedBy = "animal") // Indica o relacionamento com o campo `animal` na classe Adoption
    @JsonIgnore
    @JsonManagedReference
    private List<Adoption> adoptions; // Uma lista de todas as adoções associadas a este animal

}

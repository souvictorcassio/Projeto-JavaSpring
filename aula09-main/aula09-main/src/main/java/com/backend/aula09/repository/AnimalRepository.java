package com.backend.aula09.repository;

import com.backend.aula09.model.Animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

    // Buscar animais disponíveis (booleano)
    List<Animal> findByAvailableTrue();
}
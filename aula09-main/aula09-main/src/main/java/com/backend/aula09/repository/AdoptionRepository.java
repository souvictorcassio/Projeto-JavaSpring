package com.backend.aula09.repository;

import com.backend.aula09.model.Adoption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, UUID> {
    List<Adoption> findByUserId(UUID userId);
    List<Adoption> findByAnimalId(UUID idAnimal);
}

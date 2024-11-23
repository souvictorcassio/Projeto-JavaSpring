package com.backend.aula09.controller;

import com.backend.aula09.dto.AnimalDTO;
import com.backend.aula09.model.Animal;
import com.backend.aula09.service.AnimalService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    // POST - Criar um novo animal
    @PostMapping("/register")
    public ResponseEntity<Object> createAnimal(@RequestBody AnimalDTO animalDTO) {
        log.info("Recebida requisição para criar um novo animal.");
        try {
            Animal savedAnimal = animalService.createAnimal(animalDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o animal: " + e.getMessage());
        }
    }

    // GET - Listar todos os animais cadastrados
    @GetMapping
    public ResponseEntity<Object> getAllAnimals() {
        log.info("Recebida requisição para listar todos os animais.");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(animalService.getAllAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar os animais: " + e.getMessage());
        }
    }

    // GET - Listar apenas os animais disponíveis (available = true)
    @GetMapping("/available")
    public ResponseEntity<Object> getAvailableAnimals() {
        log.info("Recebida requisição para listar todos os animais disponíveis.");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(animalService.getAvailableAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar os animais disponíveis: " + e.getMessage());
        }
    }

    // PUT - Atualizar os dados de um animal pelo ID
    @PutMapping("/up/{id}")
    public ResponseEntity<Object> updateAnimal(@PathVariable UUID id, @RequestBody AnimalDTO animalDTO) {
        try {
            Optional<Animal> updatedAnimal = animalService.updateAnimal(id, animalDTO);
            return updatedAnimal.isPresent()
                    ? ResponseEntity.status(HttpStatus.OK).body(updatedAnimal.get())
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o animal: " + e.getMessage());
        }
    }

    // PATCH - Alterar o campo `available`
    @PatchMapping("/{id}/available")
    public ResponseEntity<Object> updateAvailability(@PathVariable UUID id, @RequestParam boolean available) {
        try {
            Optional<Animal> updatedAnimal = animalService.updateAvailability(id, available);
            return updatedAnimal.isPresent()
                    ? ResponseEntity.status(HttpStatus.OK).body("A disponibilidade do animal foi atualizada para " + (available ? "disponível" : "indisponível") + ".")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a disponibilidade do animal: " + e.getMessage());
        }
    }

    // PATCH - Atualizar localização do animal
    @PatchMapping("/{id}/location")
    public ResponseEntity<Object> updateAnimalLocation(@PathVariable UUID id, @RequestBody AnimalDTO animalDTO) {
        try {
            Optional<Animal> updatedAnimal = animalService.updateAnimalLocation(id, animalDTO);
            return updatedAnimal.isPresent()
                    ? ResponseEntity.status(HttpStatus.OK).body("A localização do animal foi atualizada com sucesso.")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a localização do animal: " + e.getMessage());
        }
    }
}


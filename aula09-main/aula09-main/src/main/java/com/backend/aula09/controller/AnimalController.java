package com.backend.aula09.controller;

import com.backend.aula09.dto.AnimalDTO;
import com.backend.aula09.model.Animal;
import com.backend.aula09.service.AnimalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Animal", description = "Endpoints para gerenciar animais")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @Operation(summary = "Cadastrar um novo animal", description = "Adiciona um novo animal ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Animal cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o animal")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> createAnimal(@RequestBody(description = "Detalhes do animal a ser cadastrado.") AnimalDTO animalDTO) {
        log.info("Recebida requisição para criar um novo animal.");
        try {
            Animal savedAnimal = animalService.createAnimal(animalDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o animal: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todos os animais", description = "Retorna todos os animais cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de animais retornada com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao listar os animais")
    })
    @GetMapping
    public ResponseEntity<Object> getAllAnimals() {
        log.info("Recebida requisição para listar todos os animais.");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(animalService.getAllAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar os animais: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar animais disponíveis", description = "Retorna apenas os animais que estão disponíveis para adoção.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de animais disponíveis retornada com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao listar os animais disponíveis")
    })
    @GetMapping("/available")
    public ResponseEntity<Object> getAvailableAnimals() {
        log.info("Recebida requisição para listar todos os animais disponíveis.");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(animalService.getAvailableAnimals());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar os animais disponíveis: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualizar animal", description = "Atualiza os detalhes de um animal existente com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar o animal")
    })
    @PutMapping("/up/{id}")
    public ResponseEntity<Object> updateAnimal(@PathVariable UUID id, @RequestBody(description = "Novos dados do animal.") AnimalDTO animalDTO) {
        try {
            Optional<Animal> updatedAnimal = animalService.updateAnimal(id, animalDTO);
            return updatedAnimal.isPresent()
                    ? ResponseEntity.status(HttpStatus.OK).body(updatedAnimal.get())
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animal não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o animal: " + e.getMessage());
        }
    }

    @Operation(summary = "Atualizar disponibilidade do animal", description = "Altera o status de disponibilidade do animal (disponível/indisponível).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar a disponibilidade")
    })
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

    @Operation(summary = "Atualizar localização do animal", description = "Atualiza a localização de um animal existente com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localização atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar a localização")
    })
    @PatchMapping("/{id}/location")
    public ResponseEntity<Object> updateAnimalLocation(@PathVariable UUID id, @RequestBody(description = "Novos dados de localização.") AnimalDTO animalDTO) {
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

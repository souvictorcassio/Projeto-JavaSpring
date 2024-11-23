package com.backend.aula09.controller;

import com.backend.aula09.dto.AdoptionDTO;
import com.backend.aula09.dto.AdoptionResponseDTO;
import com.backend.aula09.service.AdoptionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    public ResponseEntity<Object> getAllAdoptions() {
        log.info("Recebida requisição para listar todas as adoções.");
        try {
            return ResponseEntity.ok(adoptionService.getAllAdoptions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar todas as adoções: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> adoptAnimal(@RequestBody AdoptionDTO adoptionDTO) {
        log.info("Recebida requisição para registrar uma nova adoção.");
        try {
            AdoptionResponseDTO response = adoptionService.adoptAnimal(adoptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao processar adoção: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar adoção: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getAdoptionsByUser(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(adoptionService.getAdoptionsByUser(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar adoções do usuário: " + e.getMessage());
        }
    }

    @GetMapping("/with-details")
    public ResponseEntity<Object> getAllAdoptionsWithDetails() {
        try {
            return ResponseEntity.ok(adoptionService.getAllAdoptionsWithDetails());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar adoções com detalhes: " + e.getMessage());
        }
    }

    @GetMapping("/animal/{animalId}")
    public ResponseEntity<Object> getAdoptionsByAnimal(@PathVariable UUID animalId) {
        try {
            return ResponseEntity.ok(adoptionService.getAdoptionsByAnimal(animalId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar adoções do animal: " + e.getMessage());
        }
    }

    @DeleteMapping("/{adoptionId}")
    public ResponseEntity<Object> cancelAdoption(@PathVariable UUID adoptionId) {
        try {
            adoptionService.cancelAdoption(adoptionId);
            return ResponseEntity.ok("A adoção foi cancelada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar a adoção: " + e.getMessage());
        }
    }
}



package com.backend.aula09.controller;

import com.backend.aula09.dto.AdoptionDTO;
import com.backend.aula09.dto.AdoptionResponseDTO;
import com.backend.aula09.service.AdoptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/adoptions")
@Tag(name = "Adoption", description = "Endpoints para gerenciar adoções de animais")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Operation(summary = "Listar todas as adoções", description = "Busca todas as adoções realizadas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adoções retornada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar adoções.")
    })
    @GetMapping
    public ResponseEntity<Object> getAllAdoptions() {
        log.info("Recebida requisição para listar todas as adoções.");
        try {
            return ResponseEntity.ok(adoptionService.getAllAdoptions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar todas as adoções: " + e.getMessage());
        }
    }

    @Operation(summary = "Registrar uma nova adoção", description = "Registra uma nova adoção para um animal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Adoção registrada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou argumento inválido."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao registrar adoção.")
    })
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

    @Operation(summary = "Buscar adoções de um usuário", description = "Busca todas as adoções realizadas por um usuário específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adoções do usuário retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado ou sem adoções."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar adoções do usuário.")
    })
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

    @Operation(summary = "Listar todas as adoções com detalhes", description = "Busca todas as adoções realizadas com informações detalhadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adoções detalhadas retornada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar adoções detalhadas.")
    })
    @GetMapping("/with-details")
    public ResponseEntity<Object> getAllAdoptionsWithDetails() {
        try {
            return ResponseEntity.ok(adoptionService.getAllAdoptionsWithDetails());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar adoções com detalhes: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar adoções de um animal", description = "Busca todas as adoções realizadas para um animal específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adoções do animal retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado ou sem adoções."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar adoções do animal.")
    })
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

    @Operation(summary = "Cancelar uma adoção", description = "Cancela uma adoção específica pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adoção cancelada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Adoção não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao cancelar a adoção.")
    })
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

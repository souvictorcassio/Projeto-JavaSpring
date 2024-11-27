package com.backend.aula09.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Representa os dados necessários para registrar uma adoção")
public record AdoptionDTO(UUID userId, UUID animalId) {
}

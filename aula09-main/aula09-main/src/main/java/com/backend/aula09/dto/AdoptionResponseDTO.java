package com.backend.aula09.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdoptionResponseDTO(
        UUID adoptionId,
        UUID animalId,
        String animalName,
        UUID userId,
        String userName,
        String userEmail,
        LocalDateTime adoptionDate) {
}

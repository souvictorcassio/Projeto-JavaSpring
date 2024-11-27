package com.backend.aula09.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os dados necessários para registrar um animal")
public record AnimalDTO(
        String nameAnimal,
        String nameOwner,
        String emailOwner,
        String phoneOwner,
        String animalTypo,
        String animalBreed,
        String address,
        String neighborhood,
        String city,
        String state) {
}
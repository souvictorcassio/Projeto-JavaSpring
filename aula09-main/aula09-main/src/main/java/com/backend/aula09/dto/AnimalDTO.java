package com.backend.aula09.dto;

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
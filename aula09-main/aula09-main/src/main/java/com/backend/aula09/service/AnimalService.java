package com.backend.aula09.service;

import com.backend.aula09.dto.AnimalDTO;
import com.backend.aula09.model.Animal;
import com.backend.aula09.repository.AnimalRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    // Criar um novo animal
    public Animal createAnimal(AnimalDTO animalDTO) {
        try {
            Animal animal = new Animal();
            BeanUtils.copyProperties(animalDTO, animal);
            return animalRepository.save(animal);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o animal: " + e.getMessage());
        }
    }

    // Listar todos os animais
    public List<Animal> getAllAnimals() {
        try {
            return animalRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar os animais: " + e.getMessage());
        }
    }

    public List<Animal> getAvailableAnimals() {
        try {
            return animalRepository.findByAvailableTrue();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar os animais disponíveis: " + e.getMessage());
        }
    }

    // Listar apenas os animais disponíveis
    public Optional<Animal> updateAnimal(UUID id, AnimalDTO animalDTO) {
        try {
            Optional<Animal> optionalAnimal = animalRepository.findById(id);
            if (optionalAnimal.isPresent()) {
                Animal animal = optionalAnimal.get();
                BeanUtils.copyProperties(animalDTO, animal);
                animalRepository.save(animal);
            }
            return optionalAnimal;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o animal: " + e.getMessage());
        }
    }

    // Alterar o campo `available` de um animal
    public Optional<Animal> updateAvailability(UUID id, boolean available) {
        try {
            Optional<Animal> optionalAnimal = animalRepository.findById(id);
            if (optionalAnimal.isPresent()) {
                Animal animal = optionalAnimal.get();
                animal.setAvailable(available);
                animalRepository.save(animal);
            }
            return optionalAnimal;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a disponibilidade do animal: " + e.getMessage());
        }
    }

    // Atualizar os dados de localização do animal
    public Optional<Animal> updateAnimalLocation(UUID id, AnimalDTO animalDTO) {
        try {
            Optional<Animal> optionalAnimal = animalRepository.findById(id);
            if (optionalAnimal.isPresent()) {
                Animal animal = optionalAnimal.get();
                if (animalDTO.address() != null) animal.setAddress(animalDTO.address());
                if (animalDTO.neighborhood() != null) animal.setNeighborhood(animalDTO.neighborhood());
                if (animalDTO.city() != null) animal.setCity(animalDTO.city());
                if (animalDTO.state() != null) animal.setState(animalDTO.state());
                animalRepository.save(animal);
            }
            return optionalAnimal;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a localização do animal: " + e.getMessage());
        }
    }
}


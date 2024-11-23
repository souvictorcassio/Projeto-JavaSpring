package com.backend.aula09.service;

import com.backend.aula09.dto.AdoptionDTO;
import com.backend.aula09.dto.AdoptionResponseDTO;
import com.backend.aula09.model.Adoption;
import com.backend.aula09.model.Animal;
import com.backend.aula09.model.User;
import com.backend.aula09.repository.AdoptionRepository;
import com.backend.aula09.repository.AnimalRepository;
import com.backend.aula09.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

    public List<Adoption> getAllAdoptions() {
        try {
            return adoptionRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar adoções: " + e.getMessage());
        }
    }

    public AdoptionResponseDTO adoptAnimal(AdoptionDTO adoptionDTO) {
        try {
            // Buscar usuário e animal pelo ID
            Optional<User> userOptional = userRepository.findById(adoptionDTO.userId());
            Optional<Animal> animalOptional = animalRepository.findById(adoptionDTO.animalId());

            if (userOptional.isEmpty() || animalOptional.isEmpty()) {
                throw new IllegalArgumentException("Usuário ou animal inválido.");
            }

            Animal animal = animalOptional.get();

            // Verificar se o animal já foi adotado
            if (!animal.isAvailable()) {
                throw new IllegalArgumentException("O animal já foi adotado.");
            }

            // Criar um registro de adoção
            Adoption adoption = new Adoption();
            adoption.setUser(userOptional.get());
            adoption.setAnimal(animal);

            // Atualizar a disponibilidade do animal
            animal.setAvailable(false);
            animalRepository.save(animal);

            // Salvar a adoção
            Adoption savedAdoption = adoptionRepository.save(adoption);

            // Construir o DTO de resposta
            return new AdoptionResponseDTO(
                    savedAdoption.getId(),
                    animal.getId(),
                    animal.getNameAnimal(),
                    userOptional.get().getId(),
                    userOptional.get().getName(),
                    userOptional.get().getEmail(),
                    savedAdoption.getAdoptionDate()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar adoção: " + e.getMessage());
        }
    }

    public List<Adoption> getAdoptionsByUser(UUID userId) {
        try {
            if (userRepository.findById(userId).isEmpty()) {
                throw new IllegalArgumentException("Usuário não encontrado.");
            }
            return adoptionRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar adoções pelo usuário: " + e.getMessage());
        }
    }

    public List<AdoptionResponseDTO> getAllAdoptionsWithDetails() {
        try {
            List<Adoption> adoptions = adoptionRepository.findAll();

            return adoptions.stream()
                    .map(adoption -> new AdoptionResponseDTO(
                            adoption.getId(),
                            adoption.getAnimal().getId(),
                            adoption.getAnimal().getNameAnimal(),
                            adoption.getUser().getId(),
                            adoption.getUser().getName(),
                            adoption.getUser().getEmail(),
                            adoption.getAdoptionDate()
                    ))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar adoções com detalhes: " + e.getMessage());
        }
    }

    public List<Adoption> getAdoptionsByAnimal(UUID animalId) {
        try {
            if (animalRepository.findById(animalId).isEmpty()) {
                throw new IllegalArgumentException("Animal não encontrado.");
            }
            return adoptionRepository.findByAnimalId(animalId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar adoções pelo animal: " + e.getMessage());
        }
    }

    public void cancelAdoption(UUID adoptionId) {
        try {
            // Busca a adoção pelo ID
            Optional<Adoption> optionalAdoption = adoptionRepository.findById(adoptionId);

            if (optionalAdoption.isEmpty()) {
                throw new IllegalArgumentException("Adoção não encontrada.");
            }

            // Recupera a adoção e o animal associado
            Adoption adoption = optionalAdoption.get();
            Animal animal = adoption.getAnimal();

            // Define o animal como disponível novamente
            animal.setAvailable(true);
            animalRepository.save(animal);

            // Remove o registro de adoção
            adoptionRepository.delete(adoption);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar a adoção: " + e.getMessage());
        }
    }
}


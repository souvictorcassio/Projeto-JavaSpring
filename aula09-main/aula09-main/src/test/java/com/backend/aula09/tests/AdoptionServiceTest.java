package com.backend.aula09.tests;

import com.backend.aula09.dto.AdoptionDTO;
import com.backend.aula09.dto.AdoptionResponseDTO;
import com.backend.aula09.model.Adoption;
import com.backend.aula09.model.Animal;
import com.backend.aula09.model.User;
import com.backend.aula09.repository.AdoptionRepository;
import com.backend.aula09.repository.AnimalRepository;
import com.backend.aula09.repository.UserRepository;
import com.backend.aula09.service.AdoptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdoptionServiceTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AdoptionService adoptionService;

    @Test
    void shouldGetAllAdoptions() {
        // Arrange
        List<Adoption> adoptions = List.of(new Adoption());
        when(adoptionRepository.findAll()).thenReturn(adoptions);

        // Act
        List<Adoption> result = adoptionService.getAllAdoptions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adoptionRepository, times(1)).findAll();
    }

    @Test
    void shouldAdoptAnimalSuccessfully() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setNameAnimal("Max");
        animal.setAvailable(true);

        AdoptionDTO adoptionDTO = new AdoptionDTO(userId, animalId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(adoptionRepository.save(any(Adoption.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        // Act
        AdoptionResponseDTO result = adoptionService.adoptAnimal(adoptionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(animalId, result.animalId());
        assertEquals("Max", result.animalName());
        assertEquals(userId, result.userId());
        assertEquals("John Doe", result.userName());
        verify(animalRepository, times(1)).save(animal);
        verify(adoptionRepository, times(1)).save(any(Adoption.class));
    }

    @Test
    void shouldThrowExceptionWhenAdoptingUnavailableAnimal() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setAvailable(false);

        AdoptionDTO adoptionDTO = new AdoptionDTO(userId, animalId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.adoptAnimal(adoptionDTO);
        });

        assertEquals("O animal já foi adotado.", exception.getMessage());
        verify(adoptionRepository, never()).save(any());
    }

    @Test
    void shouldCancelAdoptionSuccessfully() {
        // Arrange
        UUID adoptionId = UUID.randomUUID();
        UUID animalId = UUID.randomUUID();
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setAvailable(false);

        Adoption adoption = new Adoption();
        adoption.setId(adoptionId);
        adoption.setAnimal(animal);

        when(adoptionRepository.findById(adoptionId)).thenReturn(Optional.of(adoption));

        // Act
        adoptionService.cancelAdoption(adoptionId);

        // Assert
        assertTrue(animal.isAvailable());
        verify(animalRepository, times(1)).save(animal);
        verify(adoptionRepository, times(1)).delete(adoption);
    }

    @Test
    void shouldThrowExceptionWhenCancelingNonexistentAdoption() {
        // Arrange
        UUID adoptionId = UUID.randomUUID();

        when(adoptionRepository.findById(adoptionId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.cancelAdoption(adoptionId);
        });

        assertEquals("Adoção não encontrada.", exception.getMessage());
        verify(adoptionRepository, never()).delete(any());
    }
}

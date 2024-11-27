package com.backend.aula09.tests;

import com.backend.aula09.dto.AnimalDTO;
import com.backend.aula09.model.Animal;
import com.backend.aula09.repository.AnimalRepository;
import com.backend.aula09.service.AnimalService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void shouldCreateAnimal() {
        // Arrange
        AnimalDTO animalDTO = new AnimalDTO(
                "Doggo", "John Doe", "johndoe@example.com", "123456789",
                "Dog", "Bulldog", "123 Main St", "Downtown", "CityVille", "StateLand"
        );
        Animal savedAnimal = new Animal();
        savedAnimal.setId(UUID.randomUUID());
        BeanUtils.copyProperties(animalDTO, savedAnimal);

        when(animalRepository.save(any(Animal.class))).thenReturn(savedAnimal);

        // Act
        Animal result = animalService.createAnimal(animalDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Doggo", result.getNameAnimal());
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldGetAllAnimals() {
        // Arrange
        Animal animal1 = new Animal();
        animal1.setNameAnimal("Doggo");
        Animal animal2 = new Animal();
        animal2.setNameAnimal("Kitty");

        when(animalRepository.findAll()).thenReturn(List.of(animal1, animal2));

        // Act
        List<Animal> result = animalService.getAllAnimals();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Doggo", result.get(0).getNameAnimal());
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAvailableAnimals() {
        // Arrange
        Animal animal1 = new Animal();
        animal1.setAvailable(true);
        Animal animal2 = new Animal();
        animal2.setAvailable(true);

        when(animalRepository.findByAvailableTrue()).thenReturn(List.of(animal1, animal2));

        // Act
        List<Animal> result = animalService.getAvailableAnimals();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Animal::isAvailable));
        verify(animalRepository, times(1)).findByAvailableTrue();
    }

    @Test
    void shouldUpdateAnimal() {
        // Arrange
        UUID id = UUID.randomUUID();
        Animal existingAnimal = new Animal();
        existingAnimal.setId(id);
        existingAnimal.setNameAnimal("Doggo");

        AnimalDTO updateDTO = new AnimalDTO(
                "Doggo Updated", "Jane Doe", "janedoe@example.com", "987654321",
                "Dog", "Golden Retriever", "456 Another St", "Uptown", "CityVille", "StateLand"
        );

        when(animalRepository.findById(id)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(existingAnimal);

        // Act
        Optional<Animal> result = animalService.updateAnimal(id, updateDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Doggo Updated", result.get().getNameAnimal());
        verify(animalRepository, times(1)).findById(id);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldUpdateAvailability() {
        // Arrange
        UUID id = UUID.randomUUID();
        Animal existingAnimal = new Animal();
        existingAnimal.setId(id);
        existingAnimal.setAvailable(false);

        when(animalRepository.findById(id)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(existingAnimal);

        // Act
        Optional<Animal> result = animalService.updateAvailability(id, true);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().isAvailable());
        verify(animalRepository, times(1)).findById(id);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    void shouldUpdateAnimalLocation() {
        // Arrange
        UUID id = UUID.randomUUID();
        Animal existingAnimal = new Animal();
        existingAnimal.setId(id);
        existingAnimal.setCity("OldCity");

        AnimalDTO locationDTO = new AnimalDTO(
                null, null, null, null, null, null,
                "789 New St", "NewTown", "NewCity", "NewState"
        );

        when(animalRepository.findById(id)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(existingAnimal);

        // Act
        Optional<Animal> result = animalService.updateAnimalLocation(id, locationDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("NewCity", result.get().getCity());
        assertEquals("NewState", result.get().getState());
        verify(animalRepository, times(1)).findById(id);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }
}

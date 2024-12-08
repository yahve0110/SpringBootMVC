package org.example.springbootmvc.service;

import org.example.springbootmvc.model.Pet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PetService {

    private final Map<Long, Pet> petMap;
    private Long idCounter;

    public PetService() {
        this.idCounter = 0L;
        this.petMap = new HashMap<>();
    }

    public Pet createPet(Pet petToCreate) {
        if (petToCreate == null || petToCreate.getName() == null || petToCreate.getName().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }

        Long newId = ++idCounter;
        Pet pet = new Pet(newId, petToCreate.getName(), petToCreate.getUserId());
        petMap.put(newId, pet);
        return pet;
    }

    public Pet getPetById(Long id) {
        Pet pet = petMap.get(id);
        if (pet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }
        return pet;
    }

    public void deletePet(Long id) {
        Pet pet = petMap.remove(id);
        if (pet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }
    }

    public Pet updatePet(Pet petToUpdate, Long id) {
        if (petToUpdate == null || petToUpdate.getName() == null || petToUpdate.getName().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }

        Pet existingPet = petMap.get(id);
        if (existingPet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }

        existingPet.setName(petToUpdate.getName());
        existingPet.setUserId(petToUpdate.getUserId());
        return existingPet;
    }
}

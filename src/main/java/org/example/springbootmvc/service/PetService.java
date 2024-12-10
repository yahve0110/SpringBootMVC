package org.example.springbootmvc.service;

import org.example.springbootmvc.dto.PetDto;
import org.example.springbootmvc.mapper.PetMapper;
import org.example.springbootmvc.model.Pet;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PetService {
    private final UserService userService;

    private final Map<Long, Pet> petMap;
    private Long idCounter;

    public PetService(UserService userService) {
        this.idCounter = 0L;
        this.petMap = new HashMap<>();
        this.userService = userService;
    }


    public PetDto createPet(PetDto petDtoToCreate) {
        if (petDtoToCreate == null || petDtoToCreate.getName() == null || petDtoToCreate.getName().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }

        Long newId = ++idCounter;
        Pet pet = PetMapper.toEntity(petDtoToCreate);
        pet.setId(newId);
        petMap.put(newId, pet);
        userService.addPetToUser(pet.getUserId(), pet);

        return PetMapper.toDto(pet);
    }

    public PetDto getPetById(Long id) {
        Pet pet = petMap.get(id);
        if (pet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }
        return PetMapper.toDto(pet);
    }

    public void deletePet(Long id) {
        Pet pet = petMap.remove(id);
        if (pet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }
        userService.deletePetFromUser(pet.getUserId(), id);
    }

    public PetDto updatePet(PetDto petDtoToUpdate, Long id) {
        if (petDtoToUpdate == null || petDtoToUpdate.getName() == null || petDtoToUpdate.getName().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty");
        }

        Pet existingPet = petMap.get(id);
        if (existingPet == null) {
            throw new NoSuchElementException("Pet with ID " + id + " not found");
        }

        existingPet.setName(petDtoToUpdate.getName());
        existingPet.setUserId(petDtoToUpdate.getUserId());
        userService.updatePetToUser(existingPet.getUserId(), existingPet);

        return PetMapper.toDto(existingPet);
    }
}

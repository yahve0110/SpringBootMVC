package org.example.springbootmvc.controller;

import jakarta.validation.Valid;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.service.PetService;
import org.example.springbootmvc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;
    private final UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet petToCreate) {
        Pet pet = petService.createPet(petToCreate);
        userService.addPetToUser(pet.getUserId(), pet);
        return ResponseEntity.status(201).body(pet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable("id") Long id) {
        Pet pet = petService.getPetById(id);
        if (pet != null) {
            return ResponseEntity.ok(pet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable("id") Long id) {
        Pet pet = petService.getPetById(id);
        if (pet != null) {
            userService.deletePetFromUser(pet.getUserId(), id);
            petService.deletePet(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable("id") Long id, @Valid @RequestBody Pet petToUpdate) {
        Pet existingPet = petService.getPetById(id);
        if (existingPet != null) {
            userService.updatePetToUser(existingPet.getUserId(), petToUpdate);
            Pet updatedPet = petService.updatePet(petToUpdate, id);
            return ResponseEntity.ok(updatedPet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

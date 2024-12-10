package org.example.springbootmvc.controller;

import jakarta.validation.Valid;
import org.example.springbootmvc.dto.PetDto;
import org.example.springbootmvc.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@Valid @RequestBody PetDto petDtoToCreate) {
        PetDto petDto = petService.createPet(petDtoToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(petDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable("id") Long id) {
        PetDto petDto = petService.getPetById(id);
        if (petDto != null) {
            return ResponseEntity.ok(petDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable("id") Long id) {
        petService.deletePet(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable("id") Long id, @Valid @RequestBody PetDto petDtoToUpdate) {
        PetDto updatedPetDto = petService.updatePet(petDtoToUpdate,id);
        if (updatedPetDto != null) {
            return ResponseEntity.ok(updatedPetDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

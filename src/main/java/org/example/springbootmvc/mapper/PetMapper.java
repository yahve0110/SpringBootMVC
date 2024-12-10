package org.example.springbootmvc.mapper;

import org.example.springbootmvc.dto.PetDto;
import org.example.springbootmvc.model.Pet;

public class PetMapper {

    public static PetDto toDto(Pet pet) {
        if (pet == null) {
            return null;
        }
        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getUserId()
        );
    }

    public static Pet toEntity(PetDto petDto) {
        if (petDto == null) {
            return null;
        }
        Pet pet = new Pet();
        pet.setId(petDto.getId());
        pet.setName(petDto.getName());
        pet.setUserId(petDto.getUserId());
        return pet;
    }
}

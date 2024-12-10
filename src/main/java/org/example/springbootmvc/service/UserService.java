package org.example.springbootmvc.service;

import org.example.springbootmvc.dto.UserDto;
import org.example.springbootmvc.mapper.UserMapper;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final Map<Long, User> userMap;
    private Long idCounter;

    public UserService() {
        this.idCounter = 0L;
        this.userMap = new HashMap<>();
    }

    public UserDto createUser(UserDto userDto) {
        User userToCreate = UserMapper.toEntity(userDto);

        ++idCounter;
        userToCreate.setId(idCounter);
        userMap.put(idCounter, userToCreate);

        return UserMapper.toDto(userToCreate);
    }

    public UserDto getUserById(Long id) {
        User user = userMap.get(id);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
        return UserMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        User removedUser = userMap.remove(id);
        if (removedUser == null) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        if (!userMap.containsKey(id)) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }

        User userToUpdate = userMap.get(id);
        userToUpdate.setName(userDto.getName());
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setAge(userDto.getAge());

        return UserMapper.toDto(userToUpdate);
    }

    public void addPetToUser(Long userId, Pet pet) {
        User user = userMap.get(userId);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }
        if (pet == null) {
            throw new IllegalArgumentException("Pet cannot be null");
        }
        user.getPets().add(pet);
    }

    public void deletePetFromUser(Long userId, Long petId) {
        User user = userMap.get(userId);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }

        boolean removed = user.getPets().removeIf(p -> p.getId().equals(petId));
        if (!removed) {
            throw new NoSuchElementException("Pet with ID " + petId + " not found for User ID " + userId);
        }
    }

    public void updatePetToUser(Long userId, Pet pet) {
        User user = userMap.get(userId);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }

        Pet existingPet = user.getPets().stream()
                .filter(p -> p.getId().equals(pet.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + pet.getId() + " not found for User ID " + userId));

        existingPet.setName(pet.getName());
        existingPet.setUserId(pet.getUserId());
    }
}

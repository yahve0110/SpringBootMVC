package org.example.springbootmvc.service;

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

    public User createUser(User userToCreate) {
        ++idCounter;
        User user = new User(idCounter, userToCreate.getName(), userToCreate.getEmail(), userToCreate.getAge());
        userMap.put(idCounter, user);
        return user;
    }

    public User getUserById(Long id) {
        User user = userMap.get(id);
        if (user == null) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
        return user;
    }

    public void deleteUser(Long id) {
        User removedUser = userMap.remove(id);
        if (removedUser == null) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
    }

    public User updateUser(User userToUpdate, Long id) {
        if (!userMap.containsKey(id)) {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
        userMap.put(id, userToUpdate);
        return userToUpdate;
    }

    public void addPetToUser(Long userId, Pet pet) {
        User user = getUserById(userId);
        if (pet == null) {
            throw new IllegalArgumentException("Pet cannot be null");
        }
        user.getPets().add(pet);
    }

    public void deletePetFromUser(Long userId, Long petId) {
        User user = getUserById(userId);
        boolean removed = user.getPets().removeIf(p -> p.getId().equals(petId));
        if (!removed) {
            throw new NoSuchElementException("Pet with ID " + petId + " not found for User ID " + userId);
        }
    }

    public void updatePetToUser(Long userId, Pet pet) {
        User user = getUserById(userId);
        Pet existingPet = user.getPets().stream()
                .filter(p -> p.getId().equals(pet.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Pet with ID " + pet.getId() + " not found for User ID " + userId));

        existingPet.setName(pet.getName());
        existingPet.setUserId(pet.getUserId());
    }
}

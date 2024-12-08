package org.example.springbootmvc.controller;

import jakarta.validation.Valid;
import org.example.springbootmvc.model.User;
import org.example.springbootmvc.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User userToCreate) {
        User createdUser = userService.createUser(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User foundUser = userService.getUserById(id);
        return ResponseEntity.ok(foundUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User userToUpdate) {
        User updatedUser = userService.updateUser(userToUpdate, id);
        return ResponseEntity.ok(updatedUser); // HTTP 200 OK
    }
}

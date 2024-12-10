package org.example.springbootmvc.controller;

import jakarta.validation.Valid;
import org.example.springbootmvc.dto.UserDto;
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
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userToCreate) {
        UserDto createdUser = userService.createUser(userToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        UserDto foundUser = userService.getUserById(id);
        return ResponseEntity.ok(foundUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserDto userToUpdate
    ) {
        UserDto updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }
}

package org.example.springbootmvc.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Pet {

    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private Long userId;

    public Pet() {
    }

    public Pet(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
package org.example.springbootmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootmvc.model.Pet;
import org.example.springbootmvc.model.User;
import org.example.springbootmvc.service.PetService;
import org.example.springbootmvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldCreateNewPet() throws Exception {
        var user = userService.createUser(new User(null, "Pavel", "mail@mail.ru", 25));
        var pet = new Pet(null, "Vasya", user.getId());
        String newPetJson = objectMapper.writeValueAsString(pet);

        var jsonResponse = mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPetJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var petResponse = objectMapper.readValue(jsonResponse, Pet.class);

        Assertions.assertEquals(pet.getName(), petResponse.getName());
        Assertions.assertEquals(pet.getUserId(), petResponse.getUserId());
        Assertions.assertNotNull(petResponse.getId());

        Assertions.assertDoesNotThrow(() -> petService.getPetById(petResponse.getId()));

        var userWithPet = userService.getUserById(user.getId());
        Assertions.assertEquals(1, userWithPet.getPets().size());
        Assertions.assertEquals(petResponse.getId(), userWithPet.getPets().get(0).getId());
    }

    @Test
    public void shouldGetPetById() throws Exception {
        var user = userService.createUser(new User(null, "Pavel", "mail@mail.ru", 25));
        var pet = petService.createPet(new Pet(null, "Vasya", user.getId()));

        mockMvc.perform(get("/pet/" + pet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pet.getId()))
                .andExpect(jsonPath("$.name").value(pet.getName()))
                .andExpect(jsonPath("$.userId").value(pet.getUserId()));
    }

    @Test
    public void shouldUpdatePet() throws Exception {
        var user = userService.createUser(new User(null, "Pavel", "mail@mail.ru", 25));
        var pet = petService.createPet(new Pet(null, "Vasya", user.getId()));
        pet.setName("Updated Name");

        String updatedPetJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(put("/pet/" + pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPetJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void shouldDeletePet() throws Exception {
        var user = userService.createUser(new User(null, "Pavel", "mail@mail.ru", 25));
        var pet = petService.createPet(new Pet(null, "Vasya", user.getId()));

        Assertions.assertDoesNotThrow(() -> petService.getPetById(pet.getId()));

        mockMvc.perform(delete("/pet/" + pet.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/pet/" + pet.getId()))
                .andExpect(status().isNotFound());
    }
}

package org.example.springbootmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootmvc.model.User;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUser() throws Exception {
        var user = new User(null, "BobTest", "bobTest@gmail.com", 25);

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(createdUserJson, User.class);

        Assertions.assertEquals(user.getName(), userResponse.getName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getAge(), userResponse.getAge());
        Assertions.assertNotNull(userResponse.getId());
    }

    @Test
    void getUserById() throws Exception {
        var user = new User(null, "Test2", "test2@gmail.com", 30);

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserJson, User.class);

        String retrievedUserJson = mockMvc.perform(get("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User retrievedUser = objectMapper.readValue(retrievedUserJson, User.class);

        Assertions.assertEquals(user.getName(), retrievedUser.getName());
        Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
        Assertions.assertEquals(user.getAge(), retrievedUser.getAge());
        Assertions.assertEquals(createdUser.getId(), retrievedUser.getId());
    }

    @Test
    void deleteUserById() throws Exception {
        var user = new User(null, "Test3", "test3@gmail.com", 28);

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserJson, User.class);

        mockMvc.perform(delete("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        var originalUser = new User(null, "Test4", "test4@gmail.com", 30);

        String originalUserJson = objectMapper.writeValueAsString(originalUser);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(originalUserJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserJson, User.class);

        var updatedUser = new User(createdUser.getId(), "Test4Updated", "test4updated@gmail.com", 31);

        String updatedUserJson = mockMvc.perform(put("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User userResponse = objectMapper.readValue(updatedUserJson, User.class);

        Assertions.assertEquals(updatedUser.getName(), userResponse.getName());
        Assertions.assertEquals(updatedUser.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(updatedUser.getAge(), userResponse.getAge());
        Assertions.assertEquals(updatedUser.getId(), userResponse.getId());
    }
}

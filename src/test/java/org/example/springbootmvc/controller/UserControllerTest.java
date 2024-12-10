package org.example.springbootmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootmvc.dto.UserDto;
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
        var userDto = new UserDto(null, "BobTest", "bobTest@gmail.com", 25,null);  // Создаем UserDto

        String userJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);  // Ожидаем UserDto

        Assertions.assertEquals(userDto.getName(), userResponse.getName());
        Assertions.assertEquals(userDto.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(userDto.getAge(), userResponse.getAge());
        Assertions.assertNotNull(userResponse.getId());
    }

    @Test
    void getUserById() throws Exception {
        var userDto = new UserDto(null, "Test2", "test2@gmail.com", 30,null);

        String userJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUser = objectMapper.readValue(createdUserJson, UserDto.class);

        String retrievedUserJson = mockMvc.perform(get("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto retrievedUser = objectMapper.readValue(retrievedUserJson, UserDto.class);

        Assertions.assertEquals(userDto.getName(), retrievedUser.getName());
        Assertions.assertEquals(userDto.getEmail(), retrievedUser.getEmail());
        Assertions.assertEquals(userDto.getAge(), retrievedUser.getAge());
        Assertions.assertEquals(createdUser.getId(), retrievedUser.getId());
    }

    @Test
    void deleteUserById() throws Exception {
        var userDto = new UserDto(null, "Test3", "test3@gmail.com", 28,null);

        String userJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUser = objectMapper.readValue(createdUserJson, UserDto.class);

        mockMvc.perform(delete("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/user/{id}", createdUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        var originalUserDto = new UserDto(null, "Test4", "test4@gmail.com", 30,null);

        String originalUserJson = objectMapper.writeValueAsString(originalUserDto);

        String createdUserJson = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(originalUserJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto createdUser = objectMapper.readValue(createdUserJson, UserDto.class);

        var updatedUserDto = new UserDto(createdUser.getId(), "Test4Updated", "test4updated@gmail.com", 31,null);

        String updatedUserJson = objectMapper.writeValueAsString(updatedUserDto);

        String updatedUserResponseJson = mockMvc.perform(put("/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(updatedUserResponseJson, UserDto.class);

        Assertions.assertEquals(updatedUserDto.getName(), userResponse.getName());
        Assertions.assertEquals(updatedUserDto.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(updatedUserDto.getAge(), userResponse.getAge());
        Assertions.assertEquals(updatedUserDto.getId(), userResponse.getId());
    }
}

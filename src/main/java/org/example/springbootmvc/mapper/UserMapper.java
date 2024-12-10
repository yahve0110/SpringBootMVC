package org.example.springbootmvc.mapper;

import org.example.springbootmvc.dto.UserDto;
import org.example.springbootmvc.model.User;
import java.util.stream.Collectors;


public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getPets().stream()
                        .map(PetMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());

        if (userDto.getPets() != null) {
            user.setPets(userDto.getPets().stream()
                    .map(PetMapper::toEntity)
                    .collect(Collectors.toList()));
        }
        return user;
    }
}

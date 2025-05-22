package com.ozono.ia.mapper;

import com.ozono.ia.dto.UserDto;
import com.ozono.ia.model.User;

public class UserMapper {

    public static UserDto convertToDto(User user){
        return new UserDto(user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}

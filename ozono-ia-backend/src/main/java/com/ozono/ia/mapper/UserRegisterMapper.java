package com.ozono.ia.mapper;

import com.ozono.ia.dto.UserRegisterDto;
import com.ozono.ia.model.User;

public class UserRegisterMapper {

    public static User convertToEntity(UserRegisterDto dto){
        return User.builder()
                .email(dto.email())
                .password(dto.password())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .birthDate(dto.birthDate())
                .build();
    }
}

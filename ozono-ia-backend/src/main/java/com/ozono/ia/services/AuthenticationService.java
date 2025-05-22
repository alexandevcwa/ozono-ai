package com.ozono.ia.services;

import com.ozono.ia.dto.ResponseDto;
import com.ozono.ia.dto.UserRegisterDto;

public interface AuthenticationService {

    /**
     * Authenticate a user with the given username and password.
     *
     * @param username Username of the user
     * @param password Password of the user
     * @return A token if authentication is successful, null otherwise
     */
    String authenticate(String username, String password);

    /**
     * Register a new user with the given details.
     *
     * @param dto User registration details
     * @return A token if registration is successful, null otherwise
     */
    ResponseDto register(UserRegisterDto dto);


}

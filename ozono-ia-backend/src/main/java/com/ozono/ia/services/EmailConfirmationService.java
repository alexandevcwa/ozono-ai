package com.ozono.ia.services;

public interface EmailConfirmationService {

    void confirmEmail(Integer code);

    void addConfirmationCode(String username, Integer code);

}

package com.ozono.ia.client;

import java.security.SecureRandom;

public interface CdnTokenGenerator {

    String generateToken(int length);
}

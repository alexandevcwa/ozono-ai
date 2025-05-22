package com.ozono.ia.client;

public interface CdnMediaAccessClient {

    /**
     * Check if the token is valid
     *
     * @param uuid  Image uuid
     * @param token Token to check
     * @return True if the token is valid, false otherwise
     */
    boolean isTokenValid(String uuid, String token);

    /**
     * Add a temporary token to the cache
     *
     * @param uuid  Image uuid
     * @param token Token to add
     */
    void addTemporaryToken(String uuid, String token);
}

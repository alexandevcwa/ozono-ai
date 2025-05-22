package com.ozono.ia.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CdnMediaAccessClientImpl implements CdnMediaAccessClient {

    private final Cache<String,String> accessTokensCache;
    private final PasswordEncoder passwordEncoder;

    public CdnMediaAccessClientImpl(PasswordEncoder passwordEncoder) {
        accessTokensCache = Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .maximumSize(150)
                .build();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isTokenValid(String uuid, String token){
        String sToken = accessTokensCache.getIfPresent(uuid);
        if (sToken == null) {
            return false;
        }
        return passwordEncoder.matches(token, sToken);
    }

    @Override
    public void addTemporaryToken(String uuid, String token){
        accessTokensCache.put(uuid, passwordEncoder.encode(token));
    }

}

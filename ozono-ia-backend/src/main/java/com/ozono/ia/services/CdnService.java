package com.ozono.ia.services;

import java.util.Map;

public interface CdnService {

    Map<String,Object> getCdnFileContextWithToken(String uuid, String token);
}

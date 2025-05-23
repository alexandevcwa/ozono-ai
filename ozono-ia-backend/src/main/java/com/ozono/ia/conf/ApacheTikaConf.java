package com.ozono.ia.conf;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheTikaConf {

    @Bean
    public Tika tika() {
        return new Tika();
    }

}

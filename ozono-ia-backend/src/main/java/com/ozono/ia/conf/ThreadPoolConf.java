package com.ozono.ia.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConf {

    public static final String THREAD_POOL_NAME = "OZONO-ThreadPool";

    @Bean(name = THREAD_POOL_NAME)
    Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("GCGADSS-ThreadPool-");
        executor.initialize();
        return executor;
    }
}

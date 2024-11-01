package com.ortega.transaction.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TransactionFeignConfig {

    private static final int CONNECT_TIMEOUT_MILLIS = 5000;
    private static final int READ_TIMEOUT_MILLIS = 10000;

    /**
     * Request Options for feign request.
     *
     * @return New Request options request.
     */
    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(
                CONNECT_TIMEOUT_MILLIS,
                TimeUnit.MILLISECONDS,
                READ_TIMEOUT_MILLIS,
                TimeUnit.MILLISECONDS,
                true
        );
    }

    /**
     * Request feign retryer.
     *
     * @return New Default feign retryer.
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                100,
                TimeUnit.SECONDS.toMillis(1),
                5
        );
    }

    /**
     * Feign logger level.
     *
     * @return Full logger feign level.
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

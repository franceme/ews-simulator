package com.worldpay.simulator.controller;

import com.worldpay.simulator.pojo.ExceptionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config.properties")
public class AppConfig {

    private static final int DEFAULT_ERROR_RATE = 10;
    private static final int[] DEFAULT_ERROR_CODES = new int[]{101,102,103,104,105,106,107,108,109,110};

    @Autowired
    Environment env;

    @Bean
    public ExceptionMode exceptionMode() {

        String randomExceptionsEnabledString = env.getProperty("exceptions.random.enabled");
        String avgErrorsPer200RequestsString = env.getProperty("exceptions.random.error-rate");
        String enabledRandomErrorCodesString = env.getProperty("exceptions.random.error-codes");

        boolean randomExceptionsEnabled = (randomExceptionsEnabledString != null) ? Boolean.valueOf(randomExceptionsEnabledString) : false;
        int avgErrorsPer200Requests;
        if (avgErrorsPer200RequestsString != null) {
            try {
                avgErrorsPer200Requests = Integer.valueOf(avgErrorsPer200RequestsString);
            } catch (NumberFormatException e) {
                avgErrorsPer200Requests = DEFAULT_ERROR_RATE;
            }
        } else {
            avgErrorsPer200Requests = DEFAULT_ERROR_RATE;
        }

        int[] enabledRandomErrorCodes;
        if (enabledRandomErrorCodesString != null) {
            String[] errorCodes = enabledRandomErrorCodesString.split(",");
            enabledRandomErrorCodes = new int[errorCodes.length];
            try {
                for (int i = 0; i < errorCodes.length; i++) {
                    enabledRandomErrorCodes[i] = Integer.valueOf(errorCodes[i]);
                }
            } catch (NumberFormatException e) {
                enabledRandomErrorCodes = DEFAULT_ERROR_CODES;
            }
        } else {
            enabledRandomErrorCodes = DEFAULT_ERROR_CODES;
        }

        return new ExceptionMode(randomExceptionsEnabled, avgErrorsPer200Requests, enabledRandomErrorCodes);
    }
}

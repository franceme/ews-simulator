package com.worldpay.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EWSSimulatorApplication extends SpringBootServletInitializer {
<<<<<<< HEAD
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EWSSimulatorApplication.class);
=======

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EWSSimulatorApplication.class);
>>>>>>> ba7541dbe198ccf476104054ecf65af9e34b0e3a
    }

    public static void main(String[] args) {
        SpringApplication.run(EWSSimulatorApplication.class, args);
    }
}

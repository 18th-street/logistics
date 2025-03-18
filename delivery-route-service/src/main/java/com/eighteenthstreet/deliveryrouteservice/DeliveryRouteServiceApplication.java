package com.eighteenthstreet.deliveryrouteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"exception", "base", "com.eighteenthstreet.deliveryrouteservice"})
@EnableJpaAuditing
public class DeliveryRouteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryRouteServiceApplication.class, args);
    }

}

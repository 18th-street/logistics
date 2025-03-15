package com.eighteenthstreet.deliveryagentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeliveryAgentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryAgentServiceApplication.class, args);

    }

}

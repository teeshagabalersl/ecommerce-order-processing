package org.example.ecommerceorderprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableRabbit
public class EcommerceOrderProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                EcommerceOrderProcessingApplication.class, args);
    }
}

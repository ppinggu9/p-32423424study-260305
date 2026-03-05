package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class P24260305Application {

    public static void main(String[] args) {
        SpringApplication.run(P24260305Application.class, args);
    }

}

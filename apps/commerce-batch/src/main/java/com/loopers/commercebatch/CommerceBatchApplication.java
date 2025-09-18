package com.loopers.commercebatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommerceBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceBatchApplication.class, args);
    }

}

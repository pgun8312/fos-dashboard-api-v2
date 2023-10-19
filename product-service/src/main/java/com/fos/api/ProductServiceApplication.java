package com.fos.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ProductServiceApplication {

    public static void main(String[] args) {
        log.info("Started the FOS-Product-Service successfully");
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}

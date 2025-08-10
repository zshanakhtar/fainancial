package com.fainancial.WhitelistService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class WhitelistServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhitelistServiceApplication.class, args);
        log.info("Whitelist Service Application started successfully");
    }
}
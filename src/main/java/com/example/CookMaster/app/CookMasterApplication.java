package com.example.CookMaster.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CookMasterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CookMasterApplication.class, args);
	}
}


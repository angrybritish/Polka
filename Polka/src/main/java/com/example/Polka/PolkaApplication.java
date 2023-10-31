package com.example.Polka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Controller"})
public class PolkaApplication {
	public static void main(String[] args) {
		SpringApplication.run(PolkaApplication.class, args);
	}

}

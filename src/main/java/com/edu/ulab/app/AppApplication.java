package com.edu.ulab.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AppApplication {

	public static void main(String[] args) {
		log.info("\n ------------------ app started");
		SpringApplication.run(AppApplication.class, args);
		log.info("\n ------------------ app finished");
	}

}

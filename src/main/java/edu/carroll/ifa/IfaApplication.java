package edu.carroll.ifa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class IfaApplication {

	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger(IfaApplication.class);
		logger.info("Program started");

		SpringApplication.run(IfaApplication.class, args);
	}

}

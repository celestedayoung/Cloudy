package com.cloudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableScheduling
@EnableMethodSecurity
@EnableJpaAuditing
@SpringBootApplication
public class CloudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudyApplication.class, args);
	}

}

package com.agromonitor.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AgroMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgroMonitorApplication.class, args);
	}

}

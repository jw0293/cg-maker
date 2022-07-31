package com.html.cgmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CgMakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CgMakerApplication.class, args);
	}

}

package com.jerry.imagemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImageManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageManagerApplication.class, args);
	}

}

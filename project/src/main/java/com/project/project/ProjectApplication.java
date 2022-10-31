package com.project.project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class ProjectApplication {

	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}

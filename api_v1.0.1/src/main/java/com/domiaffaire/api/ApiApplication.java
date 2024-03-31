package com.domiaffaire.api;

import com.domiaffaire.api.entities.Admin;
import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	CommandLineRunner start(UserRepository userRepository){
		return args -> {
			Admin admin = new Admin();
			admin.setEnabled(true);
			admin.setEmail("admin@gmail.com");
			admin.setPwd(new BCryptPasswordEncoder().encode("123456"));
			admin.setName("admin");
			admin.setUserRole(UserRole.ADMIN);
			userRepository.save(admin);
		};
	}

}

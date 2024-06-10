package com.domiaffaire.api;

import com.domiaffaire.api.entities.User;
import com.domiaffaire.api.enums.UserRole;
import com.domiaffaire.api.repositories.UserRepository;
import com.domiaffaire.api.services.DeadlineServiceImpl;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	CommandLineRunner start(UserRepository userRepository, DeadlineServiceImpl service){
		return args -> {
			service.checkDeadlines();
		};
	}

}

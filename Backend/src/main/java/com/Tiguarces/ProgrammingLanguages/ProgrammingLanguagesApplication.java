package com.Tiguarces.ProgrammingLanguages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProgrammingLanguagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgrammingLanguagesApplication.class, args);
	}

}

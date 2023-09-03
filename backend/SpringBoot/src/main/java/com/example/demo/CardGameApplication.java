package com.example.demo;

import com.example.demo.cardsPersistence.DecksDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class CardGameApplication {

	public static void main(String[] args) {
		if(args.length == 1) {
			DecksDatabase.setProdDbPassword(args[0]);
		}
		SpringApplication app = new SpringApplication(CardGameApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8000"));
		app.run(args);
	}

}

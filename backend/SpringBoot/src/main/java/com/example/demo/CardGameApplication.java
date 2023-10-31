package com.example.demo;

import com.example.demo.cardsPersistence.DecksDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class CardGameApplication {

	public static void main(String[] args) {
		//I disable database for production because i ran out of RDS free tier
//		if(args.length == 1) {
//			DecksDatabase.setProdDbPassword(args[0]);
//		}
//		else {
//			DecksDatabase.setProdDbPassword("1234");
//		}
		SpringApplication app = new SpringApplication(CardGameApplication.class);
		app.run(args);
	}

}

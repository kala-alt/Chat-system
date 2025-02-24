package com.example.Chat_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ChatSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatSystemApplication.class, args);
	}

}

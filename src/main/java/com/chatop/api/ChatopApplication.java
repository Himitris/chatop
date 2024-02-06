package com.chatop.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.chatop"})
public class ChatopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatopApplication.class, args);
	}

}

package com.example.demo;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootAmqpApplication {

	//https://www.youtube.com/watch?v=mKCM4alTeDw
	public static void main(String[] args) {
		SpringApplication.run(SpringBootAmqpApplication.class, args);
	}

	// This is so that SimpleMessageConverter only String, byte[] and Serializable payloads supports.
	@Bean
	public Jackson2JsonMessageConverter jsonConverter() {
		return new Jackson2JsonMessageConverter();
	}
}

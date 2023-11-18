package ru.itmo.precrimeupd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource("classpath:application.properties")
public class PrecrimeupdApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrecrimeupdApplication.class, args);
	}

}

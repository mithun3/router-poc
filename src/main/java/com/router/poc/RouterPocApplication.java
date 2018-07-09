package com.router.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @author 617150
 *
 */
//@SpringBootApplication(scanBasePackages="com.router.poc")

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:camel-route-spring.xml")
public class RouterPocApplication {
	public static void main(String[] args) {
		SpringApplication.run(RouterPocApplication.class, args);
	}
}

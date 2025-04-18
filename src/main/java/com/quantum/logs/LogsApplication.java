package com.quantum.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quantum.logs.annotations.EnableQuantumLog;

@SpringBootApplication
@EnableQuantumLog
public class LogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogsApplication.class, args);
	}

}

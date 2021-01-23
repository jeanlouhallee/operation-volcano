package com.project.operationvolcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OperationVolcanoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationVolcanoApplication.class, args);
	}

}

package com.project.operationvolcano.itest;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class OperationVolcanoApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

	@Test
	void contextLoads() {
	}

}

package com.bcdeproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.config.location=" +
		"classpath:/application.yml" +
		",classpath:/aws-test.yml"
)
@ActiveProfiles({"local"})
class BcdeprojectApplicationTests {

	@Test
	void contextLoads() {
	}

}

// travis commit & push test 11th

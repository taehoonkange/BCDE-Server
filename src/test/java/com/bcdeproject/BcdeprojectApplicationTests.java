package com.bcdeproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = "spring.config.location=" +
		"classpath:/application.yml" +
		",classpath:/aws.yml"
)
class BcdeprojectApplicationTests {

	@Test
	void contextLoads() {
	}

}

// travis commit & push test 9th

package vn.hoidanit.jobhunter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JobhunterApplicationTests {

	@Value("${spring.application.name}")
	private String title;

	@Test
	void contextLoads() {
		System.out.println("run here: " + title);
	}

}

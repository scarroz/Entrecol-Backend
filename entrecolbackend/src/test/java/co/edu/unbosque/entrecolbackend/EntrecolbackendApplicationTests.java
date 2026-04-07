package co.edu.unbosque.entrecolbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.unbosque.entrecolbackend.service.EmailService;

@SpringBootTest
class EntrecolbackendApplicationTests {

	@Test
	void contextLoads() {
		EmailService service = new EmailService();
		service.sendWelcomeEmail("sguizav@unbosque.edu.co");
	}

}

package com.ehub;

import com.ehub.controller.AuthenticationController;
import com.ehub.controller.EmailController;
import com.ehub.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EHubServiceApplicationTests {
	@Autowired
	private AuthenticationController authenticationController;

	@Autowired
	private UserController userController;

	@Autowired
	private EmailController emailController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(authenticationController);
		Assertions.assertNotNull(userController);
		Assertions.assertNotNull(emailController);
	}

}

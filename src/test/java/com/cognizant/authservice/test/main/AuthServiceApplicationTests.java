package com.cognizant.authservice.test.main;

import com.cognizant.authservice.controllers.AuthController;
import com.cognizant.authservice.main.AuthServiceApplication;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthServiceApplication.class)
@ActiveProfiles("test")
class AuthServiceApplicationTests {

	@Autowired
	private AuthController authController;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OpenAPI openAPI;

	@Test
	void contextLoads() {
		assertNotNull(authController);
	}

	@Test
	void modelMapperBean_isNotNull() {
		assertNotNull(modelMapper);
		assertInstanceOf(ModelMapper.class, modelMapper);
	}

	@Test
	void customOpenAPI_hasExpectedServer() {
		assertNotNull(openAPI);
		assertNotNull(openAPI.getServers());
		assertFalse(openAPI.getServers().isEmpty());
		assertEquals("http://localhost:9191", openAPI.getServers().get(0).getUrl());
		assertEquals("API Gateway", openAPI.getServers().get(0).getDescription());
	}

	@Test
	void main_doesNotThrow() {
		assertDoesNotThrow(() ->
				AuthServiceApplication.main(new String[]{"--spring.profiles.active=test",
						"--spring.main.web-application-type=none",
						"--spring.cloud.config.enabled=false",
						"--app.jwt.secret=PJvubyowaL8Y5ki9mcvARME+QLzAzUFVWwGjCpRsNJc="})
		);
	}
}

package com.coffee.coffee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.coffee.coffee.shared.GenericResponse;
import com.coffee.coffee.user.User;
import com.coffee.coffee.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

	private static final String API_1_0_USERS = "/api/1.0/users";
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Autowired
	UserRepository userRepository;
	
	@Before
	public void cleanUp() {
		userRepository.deleteAll();
	}
	
	
	@Test
	public void postUser_whenUserIsValid_recieveok() {
		User user = createValidUser();

		ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}


	
	@Test
	public void postUser_whenUserIsValid_userSaveToDatabase() {
		
		User user = createValidUser();

		ResponseEntity<Object> response = testRestTemplate.postForEntity(API_1_0_USERS, user, Object.class);

		
		assertThat(userRepository.count()).isEqualTo(1);

	}
	
	
	@Test
	public void postUser_whenUserIsValid_recieveSuccessMessage() {
		
		User user = createValidUser();

		ResponseEntity<GenericResponse> response = testRestTemplate.postForEntity(API_1_0_USERS, user,GenericResponse.class);

		assertThat(response.getBody().getMessage()).isNotNull();
				
	}
	
	
	@Test
	public void postUser_whenUserIsValid_passwordIsHashedInDatabase() {
		
		User user = createValidUser();

		 testRestTemplate.postForEntity(API_1_0_USERS, user,Object.class);
		 List<User> users = userRepository.findAll();
		 User inDB = users.get(0);
		 
		assertThat(inDB.getPassword()).isNotEqualTo(user.getPassword());
				
	}
	
	private User createValidUser() {
		User user = new User();
		
		user.setDisplayName("harsha");
		user.setUsername("Harsha");
		user.setPassword("password");
		return user;
	}
}

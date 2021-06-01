package com.coffee.coffee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.coffee.coffee.error.ApiError;
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
		 
		assertThat(inDB.getPassword()).isNotEqualTo(user.getPassword()); }
	

	@Test 
	public void postUser_whenUserHasNullUsername_recieveBadRequest() {
	User user = createValidUser();
	user.setUsername(null);
	ResponseEntity<Object> response = postSignUp(user,Object.class);
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}
	
	@Test
	public void postUser_whenUserHasNullDisplayname_recieveBadRequest() {
	User user = createValidUser();
	user.setDisplayName(null);
	ResponseEntity<Object> response = postSignUp(user,Object.class);
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}


	@Test
	public void postUser_whenUserNameIsLessThanRequired_recieveBadRequest() {
		User user = createValidUser();
		user.setUsername("abc");
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void  postUser_whenDisplayNameIsLessThanRequired_recieveBadRequest(){
		User user = createValidUser();
		user.setDisplayName("abe");
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void  postUser_whenPasswordIsLessThanRequired_recieveBadRequest(){
		User user = createValidUser();
		user.setPassword("pas0rd");
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenUserNameExceedsThanRequired_recieveBadRequest(){
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,1256).mapToObj(x-> "a").collect(Collectors.joining());
		user.setUsername(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenDisplayNameExceedsThanRequired_recieveBadRequest(){
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,1256).mapToObj(x-> "a").collect(Collectors.joining());
		user.setDisplayName(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void postUser_whenPasswordExceedsThanRequired_recieveBadRequest(){
		User user = createValidUser();
		String valueOf256Chars = IntStream.rangeClosed(1,1256).mapToObj(x-> "a").collect(Collectors.joining());
		user.setPassword(valueOf256Chars);
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	// @Test
	// public void postUser_whenUserIsInvalid_recieveError(){
	// 	User user = new User();
	// 	ResponseEntity<ApiError> response = postSignUp(user,ApiError.class);
	// 	assertThat(response.getBody().getUrl()).isEqualTo(API_1_0_USERS);
	// }

	// @Test
	// public void postUser_whenUserIsInvalid_recieveErrorWithValidationErrors(){
	// 	User user = new User();
	// 	ResponseEntity<ApiError> response = postSignUp(user,ApiError.class);
	// 	assertThat(response.getBody().getValidationErrors().size()).isEqualTo(3);
	// }

	
	@Test
	public void postUser_whenUserHasPasswordWithAllLowerCase_recieveBadRequest(){
		User user = createValidUser();
		user.setPassword("allowercase");
		ResponseEntity<Object> response = postSignUp(user,Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

// @Test
// public void postUser_whenAnotherUserHasSameUsername_receiveBadRequest(){

// 	userRepository.save(createValidUser());
// 	User user  = createValidUser();
// 	ResponseEntity<Object> response = postSignUp(user,Object.class);
// 	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

// }

	public <T> ResponseEntity <T> postSignUp(Object request, Class<T> response){
		return testRestTemplate.postForEntity(API_1_0_USERS,request,response);
	}
 	
	private User createValidUser() {
		User user = new User();
		
		user.setDisplayName("harsha");
		user.setUsername("harsha");
		user.setPassword("password");
		return user;
	}
}

package com.coffee.coffee.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.coffee.coffee.error.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.coffee.coffee.exception.UserNotValidException;
import com.coffee.coffee.shared.GenericResponse;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	
	@PostMapping("/api/1.0/users")
	GenericResponse createUser(@Valid @RequestBody User user) {
		 if(user.getUsername() == null || user.getDisplayName() == null) {
			 throw new UserNotValidException(); }
		userService.save(user);
		return new GenericResponse("User is saved");
	}
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
		ApiError apiError = new ApiError(400, "Validation error", request.getServletPath());
		
		// BindingResult result = exception.getBindingResult();
		// Map<String,String> validationErrors = new HashMap<>();
		//  for(FieldError fieldError: result.getFieldErrors()){
		// 	validationErrors.put(fieldError.getField(), 
		// 	fieldError.getDefaultMessage());
		
		//  }
		//  apiError.setValidationErrors(validationErrors);

		return  apiError;
	}


}

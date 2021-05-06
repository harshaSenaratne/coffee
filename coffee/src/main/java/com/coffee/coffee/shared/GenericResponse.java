package com.coffee.coffee.shared;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse {
	public GenericResponse(String message) {
		super();
		this.message = message;
	}

	private String  message;
	

}

package com.photoapp.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

	@NotNull(message = "firstName should not be null")
	@Size(min=2,message = "firstName should not be less than 2 char")
	private String firstName;
	
	@NotNull(message = "lastName should not be null")
	@Size(min=2,message = "lastName should not be less than 2 char")
	private String lastName;
	
	@NotNull(message = "password should not be null")
	@Size(min=8,max = 16,message = "password should not be between 8 and 16 char")
	private String password;
	
	@NotNull(message = "email should not be null")
	@Email
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

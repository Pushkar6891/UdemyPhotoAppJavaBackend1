package com.photoapp.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.photoapp.users.shared.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
	UserDto getUserByUserId(String userId);
		
}

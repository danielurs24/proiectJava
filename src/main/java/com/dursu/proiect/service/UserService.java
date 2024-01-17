package com.dursu.proiect.service;

import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	User registerUser(UserDto request);
	
	String loginUser(String username, String password);
}

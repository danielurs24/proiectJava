package com.dursu.proiect.service;

import com.dursu.proiect.config.JwtUtil;
import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.Role;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.exception.AuthenticationFailedException;
import com.dursu.proiect.exception.RegistrationFailedException;
import com.dursu.proiect.repository.RoleRepository;
import com.dursu.proiect.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	private JwtUtil jwtUtil;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	public User registerUser(UserDto request) throws RegistrationFailedException {
		// Check if the user already exists
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RegistrationFailedException("Username already exists");
		}
		
		Role role = roleRepository.findByName(request.getRoleName())
				.orElseThrow(() -> new RegistrationFailedException("Role not found: " + request.getRoleName()));
		
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		// Encode the password before saving
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		// Set roles based on roleNames
		user.setRole(role);
		
		// Save the user
		return userRepository.save(user);
	}
	
	public String loginUser(String username, String password) throws AuthenticationFailedException  {
		Optional<User> existingUser = userRepository.findByUsername(username);
		
		if (existingUser.isEmpty()) {
			throw new AuthenticationFailedException("Username provided does not exist");
		}
		
		if (passwordEncoder.matches(password, existingUser.get().getPassword())) {
		return jwtUtil.generateToken(existingUser.get());
		}
		
		throw new AuthenticationFailedException("Invalid username or password");
	}
	
}

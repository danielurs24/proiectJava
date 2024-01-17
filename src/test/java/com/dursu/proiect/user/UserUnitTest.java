package com.dursu.proiect.user;

import com.dursu.proiect.config.JwtUtil;
import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.Role;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.exception.AuthenticationFailedException;
import com.dursu.proiect.exception.RegistrationFailedException;
import com.dursu.proiect.repository.RoleRepository;
import com.dursu.proiect.repository.UserRepository;
import com.dursu.proiect.service.UserService;
import com.dursu.proiect.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserUnitTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private JwtUtil jwtUtil;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Test
	void testRegisterUser_Success() throws RegistrationFailedException {
		// Arrange
		
		Role role = new Role();
		role.setName("ROLE_USER");
		
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testPassword");
		user.setRole(role);
		
		UserDto userDto = new UserDto();
		userDto.setUsername("testUser");
		userDto.setPassword("testPassword");
		userDto.setRoleName("ROLE_USER");
		
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
		
		// Act
		User registeredUser = userService.registerUser(userDto);
		
		// Assert
		assertNotNull(registeredUser);
	}
	
	@Test
	void testRegisterUser_UserAlreadyExists() {
		// Arrange
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testPassword");
		
		UserDto userDto = new UserDto();
		userDto.setUsername("testUser");
		userDto.setPassword("testPassword");
		
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		
		// Act and Assert
		assertThrows(RegistrationFailedException.class, () -> userService.registerUser(userDto));
	}
	
	@Test
	void testLoginUser_Success() throws AuthenticationFailedException {
		// Arrange
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testPassword");
		
		UserDto userDto = new UserDto();
		userDto.setUsername("testUser");
		userDto.setPassword("testPassword");
		
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(jwtUtil.generateToken(any(User.class))).thenReturn("mockedToken");
		
		// Act
		String bearerToken = userService.loginUser(userDto.getUsername(), userDto.getPassword());
		
		// Assert
		assertNotNull(bearerToken);
	}
	
	@Test
	void testLoginUser_UserNotFound() {
		// Arrange
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		// Act and Assert
		assertThrows(AuthenticationFailedException.class, () -> userService.loginUser("nonExistingUser", "password"));
	}
	
	@Test
	void testLoginUser_IncorrectPassword() {
		// Arrange
		
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("incorrectPassword");
		
		UserDto userDto = new UserDto();
		userDto.setUsername("testUser");
		userDto.setPassword("testPassword");
		
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		
		// Act and Assert
		assertThrows(AuthenticationFailedException.class, () -> userService.loginUser(userDto.getUsername(), userDto.getPassword()));
	}
}

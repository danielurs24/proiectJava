package com.dursu.proiect.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dursu.proiect.controller.UserController;
import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.Role;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.exception.RegistrationFailedException;
import com.dursu.proiect.service.UserService;
import com.dursu.proiect.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserServiceImpl userService;
	
	@Test
	public void testRegisterUser() throws RegistrationFailedException {
		// Arrange
		UserDto userDto = new UserDto();
		userDto.setUsername("testUser");
		userDto.setEmail("test@example.com");
		userDto.setPassword("testPassword");
		userDto.setRoleName("USER");
		
		User registeredUser = new User();
		Role role = new Role();
		role.setName("USER");
		registeredUser.setId(1L);
		registeredUser.setUsername("testUser");
		registeredUser.setEmail("test@example.com");
		registeredUser.setRole(role);
		
		when(userService.registerUser(any(UserDto.class))).thenReturn(registeredUser);
		
		// Act
		ResponseEntity<User> response = userController.registerUser(userDto);
		
		// Assert
		verify(userService, times(1)).registerUser(userDto);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(registeredUser.getId(), response.getBody().getId());
		assertEquals(registeredUser.getUsername(), response.getBody().getUsername());
		assertEquals(registeredUser.getEmail(), response.getBody().getEmail());
		assertEquals(registeredUser.getRole().getName(), response.getBody().getRole().getName());
	}
	
	@Test
	public void testLoginAccount() throws Exception {
		// Arrange
		String username = "testUser";
		String password = "testPassword";
		String mockBearerToken = "mockBearerToken";
		when(userService.loginUser(eq(username), eq(password))).thenReturn(mockBearerToken);
		
		// Act
		ResponseEntity<ResponseDto> response = userController.loginAccount(username, password);
		
		// Assert
		verify(userService, times(1)).loginUser(username, password);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isSuccess());
		assertEquals(mockBearerToken, response.getBody().getMessage());
	}
}

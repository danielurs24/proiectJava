package com.dursu.proiect.controller;

import com.dursu.proiect.dto.ResponseDto;
import com.dursu.proiect.dto.UserDto;
import com.dursu.proiect.entity.Role;
import com.dursu.proiect.entity.User;
import com.dursu.proiect.exception.RegistrationFailedException;
import com.dursu.proiect.repository.RoleRepository;
import com.dursu.proiect.service.UserService;
import com.dursu.proiect.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "User", description = "User management APIs")
@RestController
public class UserController {
	private UserServiceImpl userService;
	
	UserController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Operation(
			summary = "Register",
			description = "Register a new user",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = User.class), mediaType = "application/json") })})
	@PostMapping("/auth/register")
	public ResponseEntity<User> registerUser(@RequestBody UserDto request) throws RegistrationFailedException {
		User registeredUser = userService.registerUser(request);
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}
	
	@Operation(
			summary = "Login",
			description = "Login with your account",
			tags = { "post" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json") })})
	@PostMapping("/auth/login")
	public ResponseEntity<ResponseDto> loginAccount(@RequestParam String username, @RequestParam String password)
			throws Exception {
		String bearerToken = this.userService.loginUser(username, password);
		return ResponseEntity.ok(new ResponseDto(true, bearerToken));
	}
}

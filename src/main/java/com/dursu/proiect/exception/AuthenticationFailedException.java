package com.dursu.proiect.exception;

public class AuthenticationFailedException extends Exception {
	public AuthenticationFailedException(String errorMessage) {
		super(errorMessage);
	}
}

package com.dursu.proiect.dto;

public class ResponseDto {
	
	private boolean success;
	private Object message;
	
	public ResponseDto(boolean success, Object message) {
		this.success = success;
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public void setMessage(Object message) {
		this.message = message;
	}
}

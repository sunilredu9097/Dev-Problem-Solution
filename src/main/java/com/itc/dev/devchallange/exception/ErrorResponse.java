package com.itc.dev.devchallange.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ErrorResponse {
	private Date timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
	List<String> subErrors;

	public ErrorResponse(Date timestamp, String message, HttpStatus status) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.status = status.value();
	}

	public ErrorResponse(Date timestamp, String message, HttpStatus status, String error) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.status = status.value();
		this.error = error;
	}

	public ErrorResponse(Date timestamp, String message, HttpStatus status, String error, String path) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.status = status.value();
		this.error = error;
		this.path = path;
	}
}

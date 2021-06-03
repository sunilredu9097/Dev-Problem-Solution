package com.itc.dev.devchallange.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), HttpStatus.BAD_REQUEST,
				"Bad Request", ((ServletWebRequest) request).getRequest().getRequestURI());
		// errorDetails.setSubErrors(subErrors);
		List<String> subErrors = ex.getConstraintViolations().stream()
				.map(x -> ((PathImpl) x.getPropertyPath()).getLeafNode().getName() + " is Required")
				.collect(Collectors.toList());
		errorDetails.setSubErrors(subErrors);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IDNumberIsNotValidSAIdException.class)
	public final ResponseEntity<ErrorResponse> handleIdNumberIsNotValid(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), HttpStatus.BAD_REQUEST,
				"Bad Request", ((ServletWebRequest) request).getRequest().getRequestURI());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataAlreadyExistException.class)
	public final ResponseEntity<ErrorResponse> handleClientAlreadyExist(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), HttpStatus.CONFLICT,
				" Already Exist", ((ServletWebRequest) request).getRequest().getRequestURI());
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> globleExcpetionHandler(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), "Something went wrong",
				HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
				((ServletWebRequest) request).getRequest().getRequestURI().toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), "Keep Mandatory Field in Request",
				HttpStatus.BAD_REQUEST, "Bad Request",
				((ServletWebRequest) request).getRequest().getRequestURI().toString());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		List<String> subErrors = new ArrayList<String>(errors.values()).stream().distinct()
				.collect(Collectors.toList());
		errorDetails.setSubErrors(subErrors);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(new Date(), ex.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found",
				((ServletWebRequest) request).getRequest().getRequestURI());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

}

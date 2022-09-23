package com.customer.offers.advice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.customer.offers.exception.ApplicationException;
import com.customer.offers.exception.BusinessException;
import com.customer.offers.exception.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorDetails> businessExceptionHandler(BusinessException businessException,
			WebRequest webRequest) {
		ErrorDetails error = new ErrorDetails(businessException.getErrorCode(), businessException.getErrorMessage(),
				new Date(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorDetails> applicationExceptionHandler(ApplicationException applicationException,
			WebRequest webRequest) {
		ErrorDetails error = new ErrorDetails(applicationException.getErrorCode(),
				applicationException.getErrorMessage(), new Date(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetails>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
	}

}

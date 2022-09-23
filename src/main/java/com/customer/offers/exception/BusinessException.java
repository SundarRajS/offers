package com.customer.offers.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorCode;

	private String errorMessage;

}

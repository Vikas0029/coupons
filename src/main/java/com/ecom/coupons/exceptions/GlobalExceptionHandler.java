package com.ecom.coupons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(HttpMessageNotReadableException.class)
	    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
	        return new ResponseEntity<>("Invalid value for CouponType: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	    }

}

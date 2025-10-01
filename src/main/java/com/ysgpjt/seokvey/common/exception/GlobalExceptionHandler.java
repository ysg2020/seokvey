package com.ysgpjt.seokvey.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SeokveyException.class)
    public ResponseEntity<SeokveyErrorResponse> handleSeokveyException(SeokveyException e) {
        SeokveyErrorResponse error = SeokveyErrorResponse.builder()
                .errorType(e.getErrorType())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }
}

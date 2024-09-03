package com.marcpinol.authservice.errors;

import com.marcpinol.authservice.exception.DuplicateUserException;
import com.marcpinol.authservice.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateUserException ex, WebRequest request) {
        ErrorMsg errorDetails = new ErrorMsg(HttpStatus.BAD_REQUEST, String.format("User with username [%s] already exists.", ex.getUserName()), new ArrayList<>());
        return handleExceptionInternal(ex, errorDetails, new HttpHeaders(), errorDetails.status(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationError> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorMsg errorDetails = new ErrorMsg(HttpStatus.BAD_REQUEST, "Validation failed", errorList);
        return handleExceptionInternal(ex, errorDetails, headers, errorDetails.status(), request);
    }
}

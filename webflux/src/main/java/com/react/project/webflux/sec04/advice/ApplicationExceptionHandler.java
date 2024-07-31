package com.react.project.webflux.sec04.advice;

import com.react.project.webflux.sec04.exception.CustomerNotFoundException;
import com.react.project.webflux.sec04.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handlerException(CustomerNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
        problem.setTitle("Customer not found");
        return problem;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handlerException(InvalidInputException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/invalid-input"));
        problem.setTitle("Invalid input");
        return problem;
    }
}

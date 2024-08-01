package com.joey.customer.portfolio.advice;

import com.joey.customer.portfolio.exceptions.CustomerNotFoundException;
import com.joey.customer.portfolio.exceptions.InsufficientBalanceException;
import com.joey.customer.portfolio.exceptions.InsufficientShareException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.function.Consumer;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handlerException(CustomerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/customer-not-found"));
            problem.setTitle("Customer not found");
        });
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handlerException(InsufficientBalanceException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/insufficient-balance"));
            problem.setTitle("Insufficient Balance");
        });
    }

    @ExceptionHandler(InsufficientShareException.class)
    public ProblemDetail handlerException(InsufficientShareException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("http://example.com/problems/insufficient-shares"));
            problem.setTitle("Insufficient Shares");
        });
    }

    private ProblemDetail build(HttpStatus httpStatus, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }
}

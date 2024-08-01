package com.joey.customer.portfolio.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    private static final String MESSAGE = "customer [id=%d] is not found";

    public CustomerNotFoundException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}

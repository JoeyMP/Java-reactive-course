package com.joey.customer.portfolio.exceptions;

public class InsufficientShareException extends RuntimeException {

    private static final String MESSAGE = "Customer [id=%d] does not have enough shares to complete the transaction";

    public InsufficientShareException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}

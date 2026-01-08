package com.sc7258.tddcafekiosk.domain.order;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(String message) {
        super(message);
    }
}

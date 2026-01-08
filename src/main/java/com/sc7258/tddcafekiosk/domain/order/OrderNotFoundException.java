package com.sc7258.tddcafekiosk.domain.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}

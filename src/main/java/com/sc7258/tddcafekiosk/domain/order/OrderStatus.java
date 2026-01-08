package com.sc7258.tddcafekiosk.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    INIT("주문 접수"),
    CANCELED("주문 취소"),
    COMPLETED("처리 완료");

    private final String text;
}

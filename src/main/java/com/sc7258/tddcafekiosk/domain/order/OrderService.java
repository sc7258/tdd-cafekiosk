package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
}

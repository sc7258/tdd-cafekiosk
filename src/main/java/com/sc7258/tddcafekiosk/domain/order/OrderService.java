package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.api.models.OrderStatusUpdateRequest; // OrderStatusUpdateRequest import 추가

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request);
}

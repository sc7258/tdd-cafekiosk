package com.sc7258.tddcafekiosk.api;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.api.models.OrderStatusUpdateRequest; // OrderStatusUpdateRequest import 추가
import com.sc7258.tddcafekiosk.domain.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderApiDelegateImpl implements OrderApiDelegate {

    private final OrderService orderService;

    public OrderApiDelegateImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderCreateRequest));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderStatus(Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, orderStatusUpdateRequest);
        return ResponseEntity.ok(updatedOrder);
    }
}

package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.api.models.OrderStatusUpdateRequest; // OrderStatusUpdateRequest import 추가
import com.sc7258.tddcafekiosk.domain.product.Product;
import com.sc7258.tddcafekiosk.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sc7258.tddcafekiosk.api.models.OrderStatus; // openapi-generator가 생성한 OrderStatus import

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        List<String> productNumbers = orderCreateRequest.getProductNumbers();
        List<Product> products = findProductsByProductNumbers(productNumbers);

        Order order = Order.create(products, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        // API 모델의 OrderStatus를 도메인 모델의 OrderStatus로 변환
        com.sc7258.tddcafekiosk.domain.order.OrderStatus newDomainStatus =
                com.sc7258.tddcafekiosk.domain.order.OrderStatus.valueOf(orderStatusUpdateRequest.getOrderStatus().name());

        order.updateStatus(newDomainStatus);
        // save를 명시적으로 호출하지 않아도 @Transactional에 의해 변경 감지(dirty checking)되어 업데이트됩니다.

        return toOrderResponse(order);
    }

    private List<Product> findProductsByProductNumbers(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setRegisteredDateTime(OffsetDateTime.of(order.getRegisteredDateTime(), ZoneOffset.systemDefault().getRules().getOffset(order.getRegisteredDateTime())));
        orderResponse.setOrderStatus(OrderStatus.fromValue(order.getOrderStatus().name()));
        orderResponse.setProducts(order.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getProduct().toProductResponse())
                .collect(Collectors.toList()));
        return orderResponse;
    }
}

package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
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

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setTotalPrice(savedOrder.getTotalPrice());
        // LocalDateTime을 OffsetDateTime으로 변환
        orderResponse.setRegisteredDateTime(OffsetDateTime.of(savedOrder.getRegisteredDateTime(), ZoneOffset.systemDefault().getRules().getOffset(savedOrder.getRegisteredDateTime())));
        // OrderStatus enum으로 변환하여 설정
        orderResponse.setOrderStatus(OrderStatus.fromValue(savedOrder.getOrderStatus().name()));

        orderResponse.setProducts(savedOrder.getOrderProducts().stream()
                        .map(orderProduct -> orderProduct.getProduct().toProductResponse())
                        .collect(Collectors.toList()));
        return orderResponse;
    }

    private List<Product> findProductsByProductNumbers(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}

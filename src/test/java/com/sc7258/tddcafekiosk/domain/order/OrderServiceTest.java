package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.domain.product.Product;
import com.sc7258.tddcafekiosk.domain.product.ProductRepository;
import com.sc7258.tddcafekiosk.domain.product.ProductSellingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList; // anyList 추가
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("여러 개의 상품 번호로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithMultipleProducts() {
        // given
        OrderCreateRequest request = new OrderCreateRequest();
        request.setProductNumbers(List.of("001", "002"));

        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        List<Product> products = List.of(product1, product2);

        // findByProductNumber 대신 findAllByProductNumberIn을 모킹합니다.
        when(productRepository.findAllByProductNumberIn(anyList()))
                .thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Simulate ID generation
            return order;
        });

        // when
        OrderResponse orderResponse = orderService.createOrder(request);

        // then
        assertThat(orderResponse.getProducts()).hasSize(2);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void createOrderWithInitStatus() {
        // given
        OrderCreateRequest request = new OrderCreateRequest();
        request.setProductNumbers(List.of("001"));

        Product product1 = createProduct("001", 1000);
        List<Product> products = List.of(product1);

        // findByProductNumber 대신 findAllByProductNumberIn을 모킹합니다.
        when(productRepository.findAllByProductNumberIn(anyList()))
                .thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Simulate ID generation
            return order;
        });

        // when
        OrderResponse orderResponse = orderService.createOrder(request);

        // then
        assertThat(orderResponse.getOrderStatus().name()).isEqualTo(com.sc7258.tddcafekiosk.domain.order.OrderStatus.INIT.name());
    }

    @DisplayName("주문 생성 시 주문 총액이 올바르게 계산된다.")
    @Test
    void createOrderWithCorrectTotalPrice() {
        // given
        OrderCreateRequest request = new OrderCreateRequest();
        request.setProductNumbers(List.of("001", "002"));

        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 2000);
        List<Product> products = List.of(product1, product2);

        // findByProductNumber 대신 findAllByProductNumberIn을 모킹합니다.
        when(productRepository.findAllByProductNumberIn(anyList()))
                .thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Simulate ID generation
            return order;
        });

        // when
        OrderResponse orderResponse = orderService.createOrder(request);

        // then
        assertThat(orderResponse.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 시간과 주문 번호가 올바르게 기록된다.")
    @Test
    void createOrderWithCorrectDateTimeAndId() {
        // given
        OrderCreateRequest request = new OrderCreateRequest();
        request.setProductNumbers(List.of("001"));

        Product product1 = createProduct("001", 1000);
        List<Product> products = List.of(product1);

        // findByProductNumber 대신 findAllByProductNumberIn을 모킹합니다.
        when(productRepository.findAllByProductNumberIn(anyList()))
                .thenReturn(products);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Simulate ID generation
            order.setRegisteredDateTime(LocalDateTime.now()); // Simulate setting registeredDateTime
            return order;
        });

        // when
        OrderResponse orderResponse = orderService.createOrder(request);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getRegisteredDateTime()).isNotNull();
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name("상품명")
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();
    }
}

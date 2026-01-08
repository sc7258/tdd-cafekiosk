package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.api.models.OrderCreateRequest;
import com.sc7258.tddcafekiosk.api.models.OrderResponse;
import com.sc7258.tddcafekiosk.api.models.OrderStatusUpdateRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
        assertThat(orderResponse.getOrderStatus().name()).isEqualTo(com.sc7258.tddcafekiosk.api.models.OrderStatus.INIT.name());
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

    @DisplayName("주문 상태를 'INIT'에서 'COMPLETED'로 변경할 수 있다.")
    @Test
    void updateOrderStatus_changesStatusToCompleted() {
        // given
        Long orderId = 1L;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderStatus(com.sc7258.tddcafekiosk.api.models.OrderStatus.COMPLETED);

        Order order = Order.builder()
                .products(List.of(createProduct("001", 4000)))
                .registeredDateTime(LocalDateTime.now())
                .build();
        order.setId(orderId);
        // order.updateStatus(com.sc7258.tddcafekiosk.domain.order.OrderStatus.INIT); // 이 줄을 제거

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        OrderResponse updatedOrderResponse = orderService.updateOrderStatus(orderId, request);

        // then
        assertThat(updatedOrderResponse.getOrderStatus()).isEqualTo(com.sc7258.tddcafekiosk.api.models.OrderStatus.COMPLETED);
    }

    @DisplayName("존재하지 않는 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    @Test
    void updateOrderStatus_throwsExceptionForNonExistentOrder() {
        // given
        Long nonExistentOrderId = 999L;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderStatus(com.sc7258.tddcafekiosk.api.models.OrderStatus.COMPLETED);

        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> orderService.updateOrderStatus(nonExistentOrderId, request))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found with id: " + nonExistentOrderId);
    }

    @DisplayName("유효하지 않은 상태로 변경하려고 하면 예외가 발생한다.")
    @Test
    void updateOrderStatus_throwsExceptionForInvalidStatusTransition() {
        // given
        Long orderId = 1L;
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setOrderStatus(com.sc7258.tddcafekiosk.api.models.OrderStatus.INIT); // COMPLETED -> INIT (유효하지 않은 전이)

        Order order = Order.builder()
                .products(List.of(createProduct("001", 4000)))
                .registeredDateTime(LocalDateTime.now())
                .build();
        order.setId(orderId);
        order.updateStatus(com.sc7258.tddcafekiosk.domain.order.OrderStatus.COMPLETED); // 이미 완료된 상태

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when // then
        assertThatThrownBy(() -> orderService.updateOrderStatus(orderId, request))
                .isInstanceOf(InvalidOrderStatusException.class)
                .hasMessageContaining("Cannot change order status from COMPLETED to INIT");
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

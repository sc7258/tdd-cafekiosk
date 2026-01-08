package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.BaseEntity;
import com.sc7258.tddcafekiosk.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime registeredDateTime;

    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return Order.builder()
                .products(products)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        if (!canUpdateStatusTo(newStatus)) {
            throw new InvalidOrderStatusException(
                    String.format("Cannot change order status from %s to %s", this.orderStatus, newStatus));
        }
        this.orderStatus = newStatus;
    }

    private boolean canUpdateStatusTo(OrderStatus newStatus) {
        // 현재 상태가 INIT일 경우 COMPLETED 또는 CANCELED로 변경 가능
        if (this.orderStatus == OrderStatus.INIT) {
            return newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELED;
        }
        // 현재 상태가 COMPLETED 또는 CANCELED일 경우 어떤 상태로도 변경 불가능
        return false;
    }

    public void setRegisteredDateTime(LocalDateTime registeredDateTime) {
        this.registeredDateTime = registeredDateTime;
    }
}

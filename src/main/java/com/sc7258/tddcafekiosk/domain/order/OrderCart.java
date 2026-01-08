package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.product.Product;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OrderCart {

    private final Map<String, OrderCartItem> cartItems = new LinkedHashMap<>();

    public Collection<OrderCartItem> getItems() {
        return cartItems.values();
    }

    public void addProduct(Product product) {
        OrderCartItem item = cartItems.get(product.getProductNumber());
        if (item != null) {
            item.increaseQuantity();
        } else {
            cartItems.put(product.getProductNumber(), new OrderCartItem(product));
        }
    }

    public void removeProduct(String productNumber) {
        cartItems.remove(productNumber);
    }

    public void clear() {
        cartItems.clear();
    }

    public void updateQuantity(String productNumber, int quantity) {
        OrderCartItem item = cartItems.get(productNumber);
        if (item == null) {
            throw new IllegalArgumentException("주문 목록에 없는 상품입니다.");
        }
        item.changeQuantity(quantity);
    }

    public int calculateTotalPrice() {
        return cartItems.values().stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}

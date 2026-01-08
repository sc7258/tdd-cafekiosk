package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.product.Product;
import lombok.Getter;

@Getter
public class OrderCartItem {

    private final Product product;
    private int quantity;

    public OrderCartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }
}

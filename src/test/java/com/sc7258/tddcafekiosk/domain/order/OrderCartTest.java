package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.product.Product;
import com.sc7258.tddcafekiosk.domain.product.ProductSellingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCartTest {

    @DisplayName("주문 목록에 있는 모든 상품의 총 금액을 올바르게 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        OrderCart cart = new OrderCart();
        Product product1 = createProduct("001", "아메리카노", 4000);
        Product product2 = createProduct("002", "카페라떼", 4500);

        cart.addProduct(product1); // 아메리카노 1개 추가
        cart.addProduct(product1); // 아메리카노 1개 더 추가 (총 2개)
        cart.addProduct(product2); // 카페라떼 1개 추가

        // when
        int totalPrice = cart.calculateTotalPrice();

        // then
        // 4000 * 2 + 4500 * 1 = 12500
        assertThat(totalPrice).isEqualTo(12500);
    }

    @DisplayName("주문 목록이 비어있을 때 총 금액은 0원이다.")
    @Test
    void calculateTotalPriceForEmptyCart() {
        // given
        OrderCart cart = new OrderCart();

        // when
        int totalPrice = cart.calculateTotalPrice();

        // then
        assertThat(totalPrice).isZero();
    }

    private Product createProduct(String productNumber, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .price(price)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();
    }
}

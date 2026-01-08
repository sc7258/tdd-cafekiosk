package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.product.Product;
import com.sc7258.tddcafekiosk.domain.product.ProductRepository;
import com.sc7258.tddcafekiosk.domain.product.ProductSellingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderCartServiceTest {

    @Autowired
    private OrderCartService orderCartService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문 목록에 상품을 추가하면, 상품 개수가 1이 된다.")
    @Test
    void addProductToCart() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        productRepository.save(product1);

        OrderCart cart = new OrderCart();

        // when
        orderCartService.addProduct(cart, product1.getProductNumber());

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().stream().findFirst().get().getQuantity()).isEqualTo(1);
    }

    @DisplayName("주문 목록에 동일한 상품을 추가하면, 수량이 1 증가한다.")
    @Test
    void addSameProductToCart() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        productRepository.save(product1);

        OrderCart cart = new OrderCart();
        orderCartService.addProduct(cart, product1.getProductNumber());

        // when
        orderCartService.addProduct(cart, product1.getProductNumber());

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().stream().findFirst().get().getQuantity()).isEqualTo(2);
    }

    @DisplayName("주문 목록에서 특정 상품을 삭제할 수 있다.")
    @Test
    void removeProductFromCart() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        Product product2 = createProduct("002", "카페라떼", 4500);
        productRepository.saveAll(List.of(product1, product2));

        OrderCart cart = new OrderCart();
        orderCartService.addProduct(cart, product1.getProductNumber());
        orderCartService.addProduct(cart, product2.getProductNumber());

        // when
        orderCartService.removeProduct(cart, product1.getProductNumber());

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().stream().findFirst().get().getProduct().getProductNumber()).isEqualTo("002");
    }

    @DisplayName("주문 목록의 모든 상품을 삭제할 수 있다.")
    @Test
    void clearCart() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        Product product2 = createProduct("002", "카페라떼", 4500);
        productRepository.saveAll(List.of(product1, product2));

        OrderCart cart = new OrderCart();
        orderCartService.addProduct(cart, product1.getProductNumber());
        orderCartService.addProduct(cart, product2.getProductNumber());

        // when
        orderCartService.clearCart(cart);

        // then
        assertThat(cart.getItems()).isEmpty();
    }

    @DisplayName("주문 목록에 담긴 상품의 개수를 변경할 수 있다.")
    @Test
    void updateQuantity() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        productRepository.save(product1);

        OrderCart cart = new OrderCart();
        orderCartService.addProduct(cart, product1.getProductNumber());

        // when
        orderCartService.updateQuantity(cart, product1.getProductNumber(), 3);

        // then
        assertThat(cart.getItems().stream().findFirst().get().getQuantity()).isEqualTo(3);
    }

    @DisplayName("상품 개수를 0개 이하로 변경하려고 하면 예외가 발생한다.")
    @Test
    void updateQuantityToZeroOrLess() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000);
        productRepository.save(product1);

        OrderCart cart = new OrderCart();
        orderCartService.addProduct(cart, product1.getProductNumber());

        // when // then
        assertThatThrownBy(() -> orderCartService.updateQuantity(cart, product1.getProductNumber(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 수량은 1개 이상이어야 합니다.");
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

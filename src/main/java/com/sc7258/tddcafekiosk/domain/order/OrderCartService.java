package com.sc7258.tddcafekiosk.domain.order;

import com.sc7258.tddcafekiosk.domain.product.Product;
import com.sc7258.tddcafekiosk.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCartService {

    private final ProductRepository productRepository;

    public void addProduct(OrderCart cart, String productNumber) {
        Product product = productRepository.findByProductNumber(productNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
        cart.addProduct(product);
    }

    public void removeProduct(OrderCart cart, String productNumber) {
        cart.removeProduct(productNumber);
    }

    public void clearCart(OrderCart cart) {
        cart.clear();
    }

    public void updateQuantity(OrderCart cart, String productNumber, int quantity) {
        cart.updateQuantity(productNumber, quantity);
    }
}

package com.sc7258.tddcafekiosk.domain.product;

import com.sc7258.tddcafekiosk.api.models.ProductCreateRequest;
import com.sc7258.tddcafekiosk.api.models.ProductResponse;
import com.sc7258.tddcafekiosk.api.models.SellingStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록하면 상품이 생성되고, 생성된 상품 정보가 반환된다.")
    @Test
    void createProduct() {
        // given
        ProductCreateRequest request = new ProductCreateRequest()
                .productNumber("001")
                .name("아메리카노")
                .price(4000)
                .sellingStatus(SellingStatus.SELLING);

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "name", "price", "sellingStatus")
                .contains("001", "아메리카노", 4000, SellingStatus.SELLING);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "name", "price", "sellingStatus")
                .contains(
                        tuple("001", "아메리카노", 4000, ProductSellingStatus.SELLING)
                );
    }

    @DisplayName("중복된 상품 번호로 상품을 등록하면 예외가 발생한다.")
    @Test
    void createProductWithDuplicateProductNumber() {
        // given
        Product product = createProduct("001", "아메리카노", 4000, ProductSellingStatus.SELLING);
        productRepository.save(product);

        ProductCreateRequest request = new ProductCreateRequest()
                .productNumber("001")
                .name("아메리카노")
                .price(4000)
                .sellingStatus(SellingStatus.SELLING);

        // when // then
        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 등록된 상품 번호입니다.");
    }

    @DisplayName("판매중인 상품 목록을 조회하면, 판매 상태가 SELLING인 상품만 조회된다.")
    @Test
    void getSellingProducts() {
        // given
        Product product1 = createProduct("001", "아메리카노", 4000, ProductSellingStatus.SELLING);
        Product product2 = createProduct("002", "카페라떼", 4500, ProductSellingStatus.HOLD);
        Product product3 = createProduct("003", "바닐라라떼", 5000, ProductSellingStatus.STOP_SELLING);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<ProductResponse> products = productService.getSellingProducts();

        // then
        assertThat(products).hasSize(1)
                .extracting("productNumber", "name", "price", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000, SellingStatus.SELLING)
                );
    }

    private Product createProduct(String productNumber, String name, int price, ProductSellingStatus sellingStatus) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .price(price)
                .sellingStatus(sellingStatus)
                .build();
    }
}

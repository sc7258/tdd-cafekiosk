package com.sc7258.tddcafekiosk.domain.product;

import com.sc7258.tddcafekiosk.api.models.ProductResponse;
import com.sc7258.tddcafekiosk.api.models.SellingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product") // 테이블 이름을 명시적으로 지정
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_number", nullable = false, unique = true)
    private String productNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "selling_status", nullable = false)
    private ProductSellingStatus sellingStatus;

    @Builder
    private Product(String productNumber, String name, int price, ProductSellingStatus sellingStatus) {
        this.productNumber = productNumber;
        this.name = name;
        this.price = price;
        this.sellingStatus = sellingStatus;
    }

    public ProductResponse toProductResponse() {
        return new ProductResponse()
                .id(this.id)
                .productNumber(this.productNumber)
                .name(this.name)
                .price(this.price)
                .sellingStatus(SellingStatus.fromValue(this.sellingStatus.name()));
    }
}

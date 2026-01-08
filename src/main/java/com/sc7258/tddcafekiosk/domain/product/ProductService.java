package com.sc7258.tddcafekiosk.domain.product;

import com.sc7258.tddcafekiosk.api.models.ProductCreateRequest;
import com.sc7258.tddcafekiosk.api.models.ProductResponse;
import com.sc7258.tddcafekiosk.api.models.SellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        validateDuplicateProductNumber(request.getProductNumber());

        Product product = Product.builder()
                .productNumber(request.getProductNumber())
                .name(request.getName())
                .price(request.getPrice())
                .sellingStatus(ProductSellingStatus.valueOf(request.getSellingStatus().getValue()))
                .build();

        Product savedProduct = productRepository.save(product);

        return toProductResponse(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(ProductSellingStatus.SELLING));

        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    private void validateDuplicateProductNumber(String productNumber) {
        productRepository.findByProductNumber(productNumber).ifPresent(product -> {
            throw new IllegalArgumentException("이미 등록된 상품 번호입니다.");
        });
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .name(product.getName())
                .price(product.getPrice())
                .sellingStatus(SellingStatus.fromValue(product.getSellingStatus().name()));
    }
}

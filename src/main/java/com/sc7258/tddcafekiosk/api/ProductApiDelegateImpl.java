package com.sc7258.tddcafekiosk.api;

import com.sc7258.tddcafekiosk.api.models.ProductCreateRequest;
import com.sc7258.tddcafekiosk.api.models.ProductResponse;
import com.sc7258.tddcafekiosk.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductApiDelegateImpl implements ProductApiDelegate {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductCreateRequest productCreateRequest) {
        ProductResponse productResponse = productService.createProduct(productCreateRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        List<ProductResponse> sellingProducts = productService.getSellingProducts();
        return ResponseEntity.ok(sellingProducts);
    }
}

package com.sc7258.tddcafekiosk.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 판매 상태가 주어진 상태 목록에 포함되는 모든 상품을 조회합니다.
     * @param sellingStatuses 판매 상태 목록
     * @return 상품 목록
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    /**
     * 상품 번호로 상품을 조회합니다.
     * @param productNumber 상품 번호
     * @return Optional<Product>
     */
    Optional<Product> findByProductNumber(String productNumber);

    /**
     * 주어진 상품 번호 목록에 포함되는 모든 상품을 조회합니다.
     * @param productNumbers 상품 번호 목록
     * @return 상품 목록
     */
    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}

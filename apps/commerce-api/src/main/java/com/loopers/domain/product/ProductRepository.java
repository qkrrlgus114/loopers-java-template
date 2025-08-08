package com.loopers.domain.product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ProductModel에서 사용하는 DB 로직 추상 메서드를 정의합니다.
 */
@Repository
public interface ProductRepository {

    Optional<Product> findById(Long productId);

    Optional<Product> register(Product product);

    List<Product> findProductListByProductId(List<Long> productIds);

    Product findByIdForUpdate(Long productId);
}

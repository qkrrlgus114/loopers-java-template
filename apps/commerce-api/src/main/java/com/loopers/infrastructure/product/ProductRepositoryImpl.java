package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductModel;
import com.loopers.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;


    @Override
    public Optional<ProductModel> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Optional<ProductModel> register(ProductModel productModel) {
        try {
            ProductModel saved = productJpaRepository.save(productModel);
            return Optional.of(saved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

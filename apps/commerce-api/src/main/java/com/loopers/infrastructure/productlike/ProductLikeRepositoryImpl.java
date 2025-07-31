package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductLikeRepositoryImpl implements ProductLikeRepository {
    private final ProductLikeJpaRepository productLikeJpaRepository;


    @Override
    public int getProductLikeCount(Long productId, Long memberId) {
        return productLikeJpaRepository.countByProductIdAndMemberId(productId, memberId);
    }

    @Override
    public Optional<ProductLike> register(ProductLike productLike) {
        try {
            ProductLike saved = productLikeJpaRepository.save(productLike);
            return Optional.of(saved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductLike> getProductLike(Long productId, Long memberId) {
        return productLikeJpaRepository.findByProductIdAndMemberId(productId, memberId);
    }

    @Override
    public void delete(ProductLike productLike) {
        productLikeJpaRepository.delete(productLike);
    }
}

package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLikeModel;
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
    public Optional<ProductLikeModel> register(ProductLikeModel productLikeModel) {
        try {
            ProductLikeModel saved = productLikeJpaRepository.save(productLikeModel);
            return Optional.of(saved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

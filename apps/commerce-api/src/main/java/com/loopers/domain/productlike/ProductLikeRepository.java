package com.loopers.domain.productlike;

import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * LikeModel에서 사용하는 DB 로직 추상 메서드를 정의합니다.
 */
@Repository
public interface ProductLikeRepository {

    int getProductLikeCount(Long productId, Long memberId);

    Optional<ProductLike> register(ProductLike productLike);

    Optional<ProductLike> getProductLike(Long productId, Long memberId);

    void delete(ProductLike productLike);
}

package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {

    int countByProductIdAndMemberId(Long productId, Long memberId);

    Optional<ProductLike> findByProductIdAndMemberId(Long productId, Long memberId);
}

package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {

    int countByProductIdAndMemberId(Long productId, Long memberId);

}

package com.loopers.infrastructure.productlike;

import com.loopers.domain.productlike.ProductLikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLikeModel, Long> {
    
    int countByProductIdAndMemberId(Long productId, Long memberId);

}

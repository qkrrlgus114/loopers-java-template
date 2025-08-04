package com.loopers.infrastructure.productlike;

import com.loopers.application.productlike.query.ProductLikeGroup;
import com.loopers.domain.productlike.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {

    Optional<ProductLike> findByProductIdAndMemberId(Long productId, Long memberId);

    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

    @Query("SELECT new com.loopers.application.productlike.query.ProductLikeGroup(pl.productId, COUNT(pl)) " +
            "FROM ProductLike pl GROUP BY pl.productId")
    List<ProductLikeGroup> countGroupByProductId();

}

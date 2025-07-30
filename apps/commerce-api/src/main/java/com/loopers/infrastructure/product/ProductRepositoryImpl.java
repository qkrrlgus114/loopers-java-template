package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductModel;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.QProductModel;
import com.loopers.domain.product.projection.ProductLikeView;
import com.loopers.domain.productlike.QProductLikeModel;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory query;


    private final QProductModel product = QProductModel.productModel;
    private final QProductLikeModel like = QProductLikeModel.productLikeModel;

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

    @Override
    public Optional<ProductLikeView> findDetailWithLikes(Long productId, Long memberId) {
        BooleanExpression likedByMeExpr = JPAExpressions
                .selectOne()
                .from(like)
                .where(like.productId.eq(productId)
                        .and(like.memberId.eq(memberId)))
                .exists();

        ProductLikeView result = query
                .select(Projections.constructor(
                        ProductLikeView.class,
                        product,
                        like.id.countDistinct(),
                        likedByMeExpr
                ))
                .from(product)
                .leftJoin(like).on(like.productId.eq(product.id))
                .where(product.id.eq(productId))
                .groupBy(product.id)
                .fetchOne();
        
        return Optional.ofNullable(result);
    }
}

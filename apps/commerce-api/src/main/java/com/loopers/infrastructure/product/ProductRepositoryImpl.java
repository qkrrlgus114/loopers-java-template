package com.loopers.infrastructure.product;

import com.loopers.application.product.result.ProductListResult;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.QProduct;
import com.loopers.support.sort.ProductSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory query;

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Optional<Product> register(Product product) {
        try {
            Product saved = productJpaRepository.save(product);
            return Optional.of(saved);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findProductListByProductId(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

    @Override
    public Product findByIdForUpdate(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 ID입니다.");
        }

        return productJpaRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. productId: " + productId));
    }

    @Override
    public List<ProductListResult> searchProducts(ProductSortType sort, int page, BigDecimal minPrice, BigDecimal maxPrice) {
        QProduct product = QProduct.product;

        List<Product> products = query.selectFrom(product)
                .where(
                        priceGoe(minPrice),
                        priceLoe(maxPrice)
                )
                .orderBy(getProductOrderSpecifier(sort))
                .offset((long) (page - 1) * 10)
                .limit(10)
                .fetch();

        return products.stream()
                .map(ProductListResult::of)
                .toList();
    }

    private BooleanExpression priceGoe(BigDecimal minPrice) {
        return minPrice != null ? QProduct.product.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(BigDecimal maxPrice) {
        return maxPrice != null ? QProduct.product.price.loe(maxPrice) : null;
    }

    private OrderSpecifier<?> getProductOrderSpecifier(ProductSortType sort) {
        if (sort == null) {
            return QProduct.product.createdAt.desc();
        }

        return switch (sort) {
            case LATEST -> QProduct.product.createdAt.desc();
            case PRICE_ASC -> QProduct.product.price.asc();
            case PRICE_DESC -> QProduct.product.price.desc();
            case NAME_ASC -> QProduct.product.name.asc();
            case NAME_DESC -> QProduct.product.name.desc();
        };
    }
}

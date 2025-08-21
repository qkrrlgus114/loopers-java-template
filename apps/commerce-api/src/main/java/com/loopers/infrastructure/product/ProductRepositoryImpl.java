package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.QProduct;
import com.loopers.support.sort.ProductSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
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
    public List<Product> searchProducts(ProductSortType sort, int page, BigDecimal minPrice, BigDecimal maxPrice, String brands) {
        QProduct product = QProduct.product;

        List<Long> productIds = query.select(product.id)
                .from(product)
                .where(
                        priceGoe(minPrice),
                        priceLoe(maxPrice),
                        brandIn(brands)
                )
                .orderBy(getProductOrderSpecifiers(sort))
                .offset((long) (page - 1) * 10)
                .limit(10)
                .fetch();

        return query.select(product)
                .from(product)
                .where(product.id.in(productIds))
                .fetch();
    }

    private BooleanExpression priceGoe(BigDecimal minPrice) {
        return minPrice != null ? QProduct.product.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(BigDecimal maxPrice) {
        return maxPrice != null ? QProduct.product.price.loe(maxPrice) : null;
    }

    private BooleanExpression brandIn(String brands) {
        if (brands == null || brands.isBlank()) {
            return null;
        }
        List<Long> brandIds = Arrays.stream(brands.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();

        if (brandIds.isEmpty()) {
            return null;
        }
        return QProduct.product.brandId.in(brandIds);
    }

    private OrderSpecifier<?>[] getProductOrderSpecifiers(ProductSortType sort) {
        QProduct p = QProduct.product;

        return switch (sort) {
            case LATEST -> new OrderSpecifier<?>[]{p.createdAt.desc(), p.id.desc()};
            case PRICE_ASC -> new OrderSpecifier<?>[]{p.price.asc(), p.id.asc()};
            case PRICE_DESC -> new OrderSpecifier<?>[]{p.price.desc(), p.id.desc()};
            case NAME_ASC -> new OrderSpecifier<?>[]{p.name.asc(), p.id.asc()};
            case NAME_DESC -> new OrderSpecifier<?>[]{p.name.desc(), p.id.desc()};
            case LIKE_COUNT_DESC -> new OrderSpecifier<?>[]{p.likeCount.desc(), p.id.desc()};
            case LIKE_COUNT_ASC -> new OrderSpecifier<?>[]{p.likeCount.asc(), p.id.asc()};
        };
    }
}

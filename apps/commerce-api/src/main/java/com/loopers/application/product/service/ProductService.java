package com.loopers.application.product.service;

import com.loopers.application.product.result.ProductListPage;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.application.product.result.ProductRegisterResult;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.interfaces.api.product.dto.ProductRegisterReqDTO;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ProductErrorType;
import com.loopers.support.sort.ProductSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ProductCache productCache;
    private final ApplicationEventPublisher publisher;

    /*
     * 상품을 조회한다.
     * */
    public Product getProductDetailById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new CoreException(ProductErrorType.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다. productId: " + productId)
        );
    }

    public Product findProductById(Long productId) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }

        return productRepository.findById(productId).orElseThrow(() ->
                new CoreException(ProductErrorType.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다. productId: " + productId)
        );
    }

    public List<Product> findProductListByProductId(List<Long> productIds) {
        return productRepository.findProductListByProductId(productIds);
    }

    public Product findProductByIdWithLock(Long productId) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }

        Product byIdWithLock = productRepository.findByIdForUpdate(productId);
        if (byIdWithLock == null) {
            throw new CoreException(ProductErrorType.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다. productId: " + productId);
        }
        return byIdWithLock;
    }

    public List<ProductListResult> getProductsWithPagingAndFilter(ProductSortType sort, int page, BigDecimal minPrice, BigDecimal maxPrice, String brands) {
        // 아무 필터링도 없는 데이터인 경우 캐싱 데이터 주면 된다.
        boolean noFilter = (minPrice == null && maxPrice == null && (brands == null || brands.isBlank()));
        boolean inCacheScope = noFilter && page >= 1 && page <= 5;

        if (inCacheScope) {
            long version = ProductCacheKeys.currentVersion(stringRedisTemplate);
            String key = ProductCacheKeys.defaultListKey(version, sort, page, 10);

            ProductListPage cacheData = productCache.getList(key);

            if (cacheData != null) {
                log.info("캐시 히트됨! 히트다 히트 : {}", key);
                return cacheData.items();
            }

            List<Product> products = productRepository.searchProducts(sort, page, minPrice, maxPrice, brands);
            List<ProductListResult> results = products.stream().map(ProductListResult::of).toList();

            productCache.putList(key, ProductListPage.of(results), 5 * 60);
            return results;
        }

        List<Product> products = productRepository.searchProducts(sort, page, minPrice, maxPrice, brands);
        return products.stream().map(ProductListResult::of).toList();
    }

    public ProductRegisterResult registerProduct(ProductRegisterReqDTO reqDTO) {
        if (reqDTO == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "상품 등록 요청이 유효하지 않습니다.");
        }

        Product product = Product.create(
                reqDTO.name(),
                reqDTO.description(),
                Long.parseLong(reqDTO.brandId()),
                Long.parseLong(reqDTO.memberId()),
                reqDTO.price()
        );

        Product savedProduct = productRepository.register(product).orElseThrow(() ->
                new CoreException(CommonErrorType.BAD_REQUEST, "상품 등록에 실패했습니다."));

        publisher.publishEvent(new ProductChangeEvent(savedProduct.getId()));

        return ProductRegisterResult.of(savedProduct);

    }


    static final class ProductCacheKeys {
        private static final String VER_KEY = "products:v1:default:ver";

        private ProductCacheKeys() {
        }

        static long currentVersion(StringRedisTemplate stringRedisTemplate) {
            String v = stringRedisTemplate.opsForValue().get(VER_KEY);
            return (v == null) ? 0L : Long.parseLong(v);
        }

        static String defaultListKey(long ver, ProductSortType sort, int page, int size) {
            String s = (sort == null) ? "DEFAULT" : sort.name();
            return String.format("products:v1:default:v=%d:sort=%s:page=%d:size=%d", ver, s, page, size);
        }
    }
}

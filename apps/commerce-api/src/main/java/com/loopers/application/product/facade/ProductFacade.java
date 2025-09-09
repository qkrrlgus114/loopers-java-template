package com.loopers.application.product.facade;

import com.loopers.application.brand.service.BrandService;
import com.loopers.application.event.EventPublisher;
import com.loopers.application.event.product.ProductDetailViewedEvent;
import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.application.product.result.ProductRegisterResult;
import com.loopers.application.product.service.ProductCache;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.productlike.service.ProductLikeService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.interfaces.api.product.dto.ProductRegisterReqDTO;
import com.loopers.support.sort.ProductSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductFacade {

    private final ProductService productService;
    private final StockService stockService;
    private final BrandService brandService;
    private final ProductLikeService productLikeService;
    private final ProductCache productCache;
    private final EventPublisher eventPublisher;

    /*
     * 상품 상세 정보 조회
     *
     * 1. 상품 정보
     * 2. 브랜드 정보
     * 3. 상품 좋아요 정보
     * */
    @Transactional
    public ProductDetailResult getProductDetail(ProductDetailCommand command) {
        // 캐시 키 생성 (좋아요 정보는 사용자별로 다르므로 memberId 포함)
        String cacheKey = String.format("product:detail:productId=%d:memberId=%d",
                command.productId(), command.memberId());

        // 캐시에서 조회
        ProductDetailResult cached = productCache.getDetail(cacheKey);
        if (cached != null) {
            log.info("상품 상세 정보 캐시 히트: {}", cacheKey);
            // 상품 상세조회 이벤트 발행
            ProductDetailViewedEvent event = ProductDetailViewedEvent.from(command.productId(), command.memberId());
            eventPublisher.publish(event);

            return cached;
        }

        // 캐시 미스 시 DB에서 조회
        Product product = productService.getProductDetailById(command.productId());
        Brand brand = brandService.getBrandDetail(product.getBrandId());
        boolean likedByMember = productLikeService.isLikedByMember(command.productId(), command.memberId());

        ProductDetailResult result = ProductDetailResult.of(product, brand, likedByMember);

        // 캐시에 저장 (10분)
        productCache.putDetail(cacheKey, result, 10 * 60 * 1000);

        // 상품 상세조회 이벤트 발행
        ProductDetailViewedEvent event = ProductDetailViewedEvent.from(command.productId(), command.memberId());
        eventPublisher.publish(event);
        log.debug("상품 상세조회 이벤트 발행 - productId: {}, memberId: {}", command.productId(), command.memberId());

        return result;
    }

    /*
     * 상품 리스트 조회(페이징 + 필터링)
     * */
    @Transactional(readOnly = true)
    public List<ProductListResult> getProductsWithPagingAndFilter(
            ProductSortType sort, int page, BigDecimal minPrice, BigDecimal maxPrice, String keyword
    ) {
        return productService.getProductsWithPagingAndFilter(sort, page, minPrice, maxPrice, keyword);
    }

    /*
     * 상품 등록
     * */
    @Transactional
    public ProductRegisterResult registerProduct(ProductRegisterReqDTO reqDTO) {
        ProductRegisterResult productRegisterResult = productService.registerProduct(reqDTO);
        stockService.registerStock(productRegisterResult.productId(), reqDTO.stock());

        return productRegisterResult;
    }
}

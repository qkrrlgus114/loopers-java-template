package com.loopers.application.product.facade;

import com.loopers.application.brand.service.BrandService;
import com.loopers.application.event.EventPublisher;
import com.loopers.application.event.product.ProductDetailViewedEvent;
import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.PopularProductResult;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.application.product.result.ProductRegisterResult;
import com.loopers.application.product.service.ProductCache;
import com.loopers.application.product.service.ProductRankingService;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final ProductRankingService productRankingService;
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

        // 오늘의 랭킹 순위 조회
        Long rank = productRankingService.getTodayRank(command.productId());

        ProductDetailResult result = ProductDetailResult.of(product, brand, likedByMember, rank);

        // 캐시에 저장 (10분)
        productCache.putDetail(cacheKey, result, 10 * 60 * 1000);

        // 상품 상세조회 이벤트 발행
        ProductDetailViewedEvent event = ProductDetailViewedEvent.from(command.productId(), command.memberId());
        eventPublisher.publish(event);
        log.debug("상품 상세조회 이벤트 발행 - productId: {}, memberId: {}, rank: {}",
                command.productId(), command.memberId(), rank);

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

    /*
     * 오늘의 인기상품 조회 (랭킹 기반)
     */
    @Transactional(readOnly = true)
    public List<PopularProductResult> getTodayPopularProducts(int page, int limit) {
        // Redis ZSET에서 상위 랭킹 상품 ID 조회 (페이징 처리)
        List<Long> topProductIds = productRankingService.getTodayTopProductIds(page, limit);

        if (topProductIds.isEmpty()) {
            log.info("오늘의 인기상품이 없습니다. - page: {}, limit: {}", page, limit);
            return List.of();
        }

        // 상품 정보 조회
        List<Product> products = productService.findProductListByProductId(topProductIds);

        // 순위 정보와 함께 결과 생성
        List<PopularProductResult> results = new ArrayList<>();

        // 실제 순위 계산 (페이지를 고려한 순위)
        int startRank = (page - 1) * limit + 1;

        for (int i = 0; i < topProductIds.size(); i++) {
            Long productId = topProductIds.get(i);

            // 해당 상품 찾기
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (product != null) {
                Double score = productRankingService.getTodayScore(productId);
                Long rank = (long) (startRank + i); // 페이지를 고려한 실제 순위

                results.add(PopularProductResult.of(product, score, rank));
            }
        }

        log.info("오늘의 인기상품 조회 완료 - page: {}, limit: {}, 결과: {} 건", page, limit, results.size());
        return results;
    }

    /*
     * 특정 날짜의 상품 랭킹 조회 (페이징 기반)
     */
    @Transactional(readOnly = true)
    public List<PopularProductResult> getRankingsByDate(LocalDate date, int page, int size) {
        // Redis ZSET에서 특정 날짜의 랭킹 상품 ID 조회
        List<Long> rankingProductIds = productRankingService.getRankingProductIdsByDate(date, page, size);

        if (rankingProductIds.isEmpty()) {
            log.info("해당 날짜의 랭킹 데이터가 없습니다. - date: {}, page: {}, size: {}", date, page, size);
            return List.of();
        }

        // 상품 정보 조회
        List<Product> products = productService.findProductListByProductId(rankingProductIds);

        // 순위 정보와 함께 결과 생성
        List<PopularProductResult> results = new ArrayList<>();

        // 실제 순위 계산 (페이지를 고려한 순위)
        int startRank = (page - 1) * size + 1;

        for (int i = 0; i < rankingProductIds.size(); i++) {
            Long productId = rankingProductIds.get(i);

            // 해당 상품 찾기
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (product != null) {
                Double score = productRankingService.getScoreByDate(productId, date);
                Long rank = (long) (startRank + i); // 페이지를 고려한 실제 순위

                results.add(PopularProductResult.of(product, score, rank));
            }
        }

        log.info("특정 날짜 랭킹 조회 완료 - date: {}, page: {}, size: {}, 결과: {} 건", date, page, size, results.size());
        return results;
    }
}

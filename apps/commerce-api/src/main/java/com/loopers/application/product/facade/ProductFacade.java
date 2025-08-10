package com.loopers.application.product.facade;

import com.loopers.application.brand.service.BrandService;
import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.productlike.service.ProductLikeService;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
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
    private final BrandService brandService;
    private final ProductLikeService productLikeService;

    /*
     * 상품 상세 정보 조회
     *
     * 1. 상품 정보
     * 2. 브랜드 정보
     * 3. 상품 좋아요 정보
     * */
    @Transactional(readOnly = true)
    public ProductDetailResult getProductDetail(ProductDetailCommand command) {
        Product product = productService.getProductDetailById(command.productId());
        Brand brand = brandService.getBrandDetail(product.getBrandId());
        boolean likedByMember = productLikeService.isLikedByMember(command.productId(), command.memberId());


        return ProductDetailResult.of(product, brand, likedByMember);
    }

    /*
     * 상품 검색 기능
     * */
    @Transactional(readOnly = true)
    public List<ProductListResult> searchProducts(
            ProductSortType sort, int page, BigDecimal minPrice, BigDecimal maxPrice
    ) {
        return productService.searchProducts(sort, page, minPrice, maxPrice);
    }

}

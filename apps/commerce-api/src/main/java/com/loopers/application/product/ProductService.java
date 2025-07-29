package com.loopers.application.product;

import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.domain.brand.BrandModel;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.product.ProductModel;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.productlike.ProductLikeRepository;
import com.loopers.support.error.BrandErrorType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ProductErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductLikeRepository productLikeRepository;

    /*
     * 상품 상세 조회
     * */
    @Transactional(readOnly = true)
    public ProductDetailResult getProductDetail(ProductDetailCommand command) {
        ProductModel productModel = productRepository.findById(command.productId()).orElseThrow(() ->
                new CoreException(ProductErrorType.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다. productId: " + command.productId())
        );

        BrandModel brandModel = brandRepository.findById(productModel.getBrandId()).orElseThrow(() ->
                new CoreException(BrandErrorType.BRAND_NOT_FOUND, "상품의 브랜드를 찾을 수 없습니다. productId: " + command.productId())
        );

        int likeCnt = productLikeRepository.getProductLikeCount(command.productId(), command.memberId());

        return ProductDetailResult.of(productModel, brandModel, likeCnt);
    }
}

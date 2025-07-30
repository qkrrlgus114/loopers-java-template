package com.loopers.application.product.service;

import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.projection.ProductLikeView;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ProductErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*
     * 상품을 조회한다.
     * */
    public ProductLikeView getProductDetail(Long productId, Long memberId) {
        return productRepository.findDetailWithLikes(productId, memberId).orElseThrow(() ->
                new CoreException(ProductErrorType.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다. productId: " + productId)
        );
    }
}

package com.loopers.application.product.service;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ProductErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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
}

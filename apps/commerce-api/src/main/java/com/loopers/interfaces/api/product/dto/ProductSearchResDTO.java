package com.loopers.interfaces.api.product.dto;

import com.loopers.application.product.result.ProductListResult;

import java.util.List;

public record ProductSearchResDTO(
        List<ProductListResult> productList
) {
    public static ProductSearchResDTO of(List<ProductListResult> productList) {
        return new ProductSearchResDTO(productList);
    }
}

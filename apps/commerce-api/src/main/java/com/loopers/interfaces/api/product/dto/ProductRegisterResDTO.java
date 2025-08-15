package com.loopers.interfaces.api.product.dto;

import java.math.BigDecimal;

public record ProductRegisterResDTO(
        Long productId,
        String name,
        String description,
        String brandId,
        String status,
        int likeCount,
        BigDecimal price
) {
    public static ProductRegisterResDTO of(Long productId, String name, String description, String brandId, String status, int likeCount, BigDecimal price) {
        return new ProductRegisterResDTO(productId, name, description, brandId, status, likeCount, price);
    }
}

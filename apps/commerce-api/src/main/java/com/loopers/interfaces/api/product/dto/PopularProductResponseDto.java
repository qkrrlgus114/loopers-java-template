package com.loopers.interfaces.api.product.dto;

import com.loopers.application.product.result.PopularProductResult;
import com.loopers.domain.product.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record PopularProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        int likeCount,
        Double rankingScore,
        Long rank
) {
    
    public static PopularProductResponseDto of(PopularProductResult result) {
        return new PopularProductResponseDto(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                result.status(),
                result.likeCount(),
                result.rankingScore(),
                result.rank()
        );
    }
    
    public static List<PopularProductResponseDto> of(List<PopularProductResult> results) {
        return results.stream()
                .map(PopularProductResponseDto::of)
                .toList();
    }
}
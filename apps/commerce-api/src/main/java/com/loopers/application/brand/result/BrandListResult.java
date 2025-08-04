package com.loopers.application.brand.result;

import com.loopers.domain.brand.Brand;

public record BrandListResult(
        Long id,
        String name,
        String description,
        Long memberId
) {

    public static BrandListResult toResult(Brand model) {
        return new BrandListResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getMemberId()
        );
    }
}

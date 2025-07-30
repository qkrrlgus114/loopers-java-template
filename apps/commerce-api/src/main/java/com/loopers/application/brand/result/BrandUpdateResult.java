package com.loopers.application.brand.result;

import com.loopers.domain.brand.Brand;

public record BrandUpdateResult(
        Long id,
        String name,
        String description,
        Long memberId
) {
    public static BrandUpdateResult of(Brand model) {

        return new BrandUpdateResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getMemberId()
        );
    }
}

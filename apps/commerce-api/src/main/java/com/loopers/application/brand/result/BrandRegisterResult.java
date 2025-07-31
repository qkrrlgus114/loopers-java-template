package com.loopers.application.brand.result;

import com.loopers.domain.brand.Brand;

public record BrandRegisterResult(
        Long id,
        String name,
        String description,
        Long memberId
) {
    public static BrandRegisterResult of(Brand model) {

        return new BrandRegisterResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getMemberId()
        );
    }
}

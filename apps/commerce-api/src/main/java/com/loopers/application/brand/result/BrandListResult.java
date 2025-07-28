package com.loopers.application.brand.result;

import com.loopers.domain.brand.BrandModel;

public record BrandListResult(
        Long id,
        String name,
        String description,
        Long memberId
) {

    public static BrandListResult toResult(BrandModel model) {
        return new BrandListResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getMemberId()
        );
    }
}

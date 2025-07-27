package com.loopers.application.brand.result;

import com.loopers.domain.brand.BrandModel;

public record BrandRegisterResult(
        Long id,
        String name,
        String description,
        Long memberId
) {
    public static BrandRegisterResult of(BrandModel model) {

        return new BrandRegisterResult(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getMemberId()
        );
    }
}

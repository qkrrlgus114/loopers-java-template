package com.loopers.application.brand.command;

public record BrandUpdateCommand(
        Long id,
        String name,
        String description,
        Long memberId
) {
}

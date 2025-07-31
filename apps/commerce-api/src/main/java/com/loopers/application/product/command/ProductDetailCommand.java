package com.loopers.application.product.command;

import jakarta.validation.constraints.NotNull;

public record ProductDetailCommand(

        @NotNull
        Long productId,
        @NotNull
        Long memberId
) {
}

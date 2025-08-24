package com.loopers.interfaces.api.product.dto;

import java.math.BigDecimal;

public record ProductRegisterReqDTO(
        String memberId,
        String brandId,
        String description,
        String name,
        Integer stock,
        BigDecimal price
) {
}

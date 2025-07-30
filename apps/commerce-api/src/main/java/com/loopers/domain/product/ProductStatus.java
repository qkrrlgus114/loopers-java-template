package com.loopers.domain.product;

import lombok.Getter;

@Getter
public enum ProductStatus {
    REGISTERED("등록됨"),
    SELLING("판매중"),
    SOLD_OUT("품절");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

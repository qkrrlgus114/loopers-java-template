package com.loopers.interfaces.api.brand.dto;

import com.loopers.application.brand.result.BrandRegisterResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BrandDTO {

    public static record BrandRegisterRequest(
            @NotBlank
            @Size(min = 1, max = 20, message = "브랜드 이름은 1자 이상 20자 이하로 입력해주세요.")
            String name,
            @NotBlank
            String description,
            @NotNull
            Long memberId
    ) {
    }

    public static record BrandRegisterResponse(
            Long id,
            String name,
            String description,
            Long memberId
    ) {
        public static BrandRegisterResponse from(BrandRegisterResult result) {
            return new BrandRegisterResponse(
                    result.id(),
                    result.name(),
                    result.description(),
                    result.memberId()
            );
        }
    }
}

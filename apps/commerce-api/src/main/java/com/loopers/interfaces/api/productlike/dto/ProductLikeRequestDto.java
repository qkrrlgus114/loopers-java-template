package com.loopers.interfaces.api.productlike.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "상품 좋아요 요청 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeRequestDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "회원 ID", example = "1")
    private Long memberId;
}

package com.loopers.interfaces.api.productlike.dto;

import com.loopers.application.productlike.result.ProductLikeResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "상품 좋아요 응답 DTO")
@Getter
@Builder
public class ProductLikeResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "좋아요 여부", example = "true")
    private boolean isLiked;

    @Schema(description = "좋아요 수", example = "10")
    private int likeCount;

    @Schema(description = "처리 성공 여부", example = "true")
    private boolean isSuccess;

    public static ProductLikeResponseDto of(ProductLikeResult result) {
        return ProductLikeResponseDto.builder()
                .productId(result.getProductId())
                .isLiked(result.isLiked())
                .likeCount(result.getLikeCount())
                .isSuccess(result.isStatus())
                .build();
    }
}

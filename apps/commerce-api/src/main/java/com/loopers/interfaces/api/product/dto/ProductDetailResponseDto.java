package com.loopers.interfaces.api.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loopers.application.product.result.ProductDetailResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Schema(description = "상품 상세 조회 응답 DTO")
@Getter
@Builder
public class ProductDetailResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "상품1")
    private String name;

    @Schema(description = "상품 설명", example = "상품 설명입니다.")
    private String description;

    @Schema(description = "가격", example = "10000")
    private BigDecimal price;

    @Schema(description = "브랜드 ID", example = "1")
    private Long brandId;

    @Schema(description = "브랜드명", example = "브랜드1")
    private String brandName;

    @Schema(description = "좋아요 수", example = "10")
    private int likeCount;

    @Schema(description = "사용자 좋아요 여부", example = "true")
    private Boolean isLiked;

    @Schema(description = "오늘의 인기 순위", example = "1")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Long rank;

    public static ProductDetailResponseDto of(ProductDetailResult result) {
        return ProductDetailResponseDto.builder()
                .id(result.id())
                .name(result.name())
                .description(result.description())
                .price(result.price())
                .brandId(result.brandId())
                .brandName(result.brandName())
                .likeCount(result.likeCount())
                .isLiked(result.isLiked())
                .rank(result.rank())
                .build();
    }
}

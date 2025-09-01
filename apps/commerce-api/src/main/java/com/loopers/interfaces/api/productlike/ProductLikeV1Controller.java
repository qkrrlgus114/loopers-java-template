package com.loopers.interfaces.api.productlike;

import com.loopers.application.productlike.command.ProductLikeCommand;
import com.loopers.application.productlike.facade.ProductLikeFacade;
import com.loopers.application.productlike.result.ProductLikeResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.productlike.dto.ProductLikeRequestDto;
import com.loopers.interfaces.api.productlike.dto.ProductLikeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 좋아요", description = "상품 좋아요 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-likes")
public class ProductLikeV1Controller {

    private final ProductLikeFacade productLikeFacade;

    @Operation(summary = "상품 좋아요 등록", description = "상품에 좋아요를 등록합니다.")
    @PostMapping
    public ApiResponse<ProductLikeResponseDto> registerProductLike(@RequestBody ProductLikeRequestDto requestDto) {
        ProductLikeCommand command = ProductLikeCommand.of(requestDto.getProductId(), requestDto.getMemberId());

        ProductLikeResult result = productLikeFacade.registerProductLike(command);

        return ApiResponse.success(ProductLikeResponseDto.of(result));
    }

    @Operation(summary = "상품 좋아요 취소", description = "상품 좋아요를 취소합니다.")
    @DeleteMapping
    public ApiResponse<ProductLikeResponseDto> cancelProductLike(@RequestBody ProductLikeRequestDto requestDto) {
        ProductLikeCommand command = ProductLikeCommand.of(requestDto.getProductId(), requestDto.getMemberId());
        ProductLikeResult result = productLikeFacade.cancelProductLike(command);
        return ApiResponse.success(ProductLikeResponseDto.of(result));
    }
}

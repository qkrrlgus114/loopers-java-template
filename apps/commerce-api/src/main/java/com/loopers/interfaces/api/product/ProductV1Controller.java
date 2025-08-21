package com.loopers.interfaces.api.product;

import com.loopers.application.product.facade.ProductFacade;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.result.ProductRegisterResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.dto.ProductDetailResponseDto;
import com.loopers.interfaces.api.product.dto.ProductRegisterReqDTO;
import com.loopers.interfaces.api.product.dto.ProductRegisterResDTO;
import com.loopers.interfaces.api.product.dto.ProductSearchResDTO;
import com.loopers.support.sort.ProductSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class ProductV1Controller {

    private final ProductFacade productFacade;

    /*
     * 상품 목록 조회(페이징 + 정렬)
     * */
    @GetMapping("/products")
    public ApiResponse<?> getProducts(
            @RequestParam(value = "sort", required = false, defaultValue = "LATEST") ProductSortType sort,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "brands", required = false) String brands) {
        List<ProductListResult> productListResults = productFacade.getProductsWithPagingAndFilter(sort, page, minPrice, maxPrice, brands);

        ProductSearchResDTO productSearchResDTO = ProductSearchResDTO.of(productListResults);

        return ApiResponse.success(productSearchResDTO);
    }

    /*
     * 상품 상세 조회
     * */
    @GetMapping("/products/{productId}")
    public ApiResponse<ProductDetailResponseDto> getProductDetail(
            @PathVariable("productId") Long productId,
            @RequestParam("memberId") Long memberId) {
        ProductDetailCommand command = new ProductDetailCommand(productId, memberId);
        ProductDetailResult productDetailResult = productFacade.getProductDetail(command);
        return ApiResponse.success(ProductDetailResponseDto.of(productDetailResult));
    }

    /*
     * 상품 등록
     * */
    @PostMapping("/products")
    public ApiResponse<?> createProduct(@RequestBody @Validated ProductRegisterReqDTO reqDTO) {
        ProductRegisterResult productRegisterResult = productFacade.registerProduct(reqDTO);

        ProductRegisterResDTO productRegisterResDTO = ProductRegisterResDTO.of(
                productRegisterResult.productId(),
                productRegisterResult.name(),
                productRegisterResult.description(),
                productRegisterResult.brandId(),
                productRegisterResult.status(),
                productRegisterResult.likeCount(),
                productRegisterResult.price()
        );

        return ApiResponse.success(productRegisterResDTO);
    }

}

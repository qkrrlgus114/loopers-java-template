package com.loopers.interfaces.api.product;

import com.loopers.application.product.facade.ProductFacade;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.dto.ProductSearchResDTO;
import com.loopers.support.sort.ProductSortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "sort", required = false) ProductSortType sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice) {
        List<ProductListResult> productListResults = productFacade.searchProducts(sort, page, minPrice, maxPrice);

        ProductSearchResDTO productSearchResDTO = ProductSearchResDTO.of(productListResults);

        return ApiResponse.success(productSearchResDTO);
    }

}

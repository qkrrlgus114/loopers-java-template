package com.loopers.interfaces.api;

import com.loopers.application.product.facade.ProductFacade;
import com.loopers.application.product.result.ProductListResult;
import com.loopers.support.sort.ProductSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping("/search")
    public List<ProductListResult> searchProducts(
            @RequestParam(required = false, defaultValue = "LATEST") ProductSortType sort,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        return productFacade.searchProducts(sort, page, minPrice, maxPrice);
    }
}

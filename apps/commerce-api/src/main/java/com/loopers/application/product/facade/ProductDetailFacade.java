package com.loopers.application.product.facade;

import com.loopers.application.brand.service.BrandService;
import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.application.product.service.ProductService;
import com.loopers.domain.brand.BrandModel;
import com.loopers.domain.product.projection.ProductLikeView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDetailFacade {

    private final ProductService productService;
    private final BrandService brandService;

    /*
     * 상품 상세 정보 조회
     * */
    @Transactional(readOnly = true)
    public ProductDetailResult getProductDetail(ProductDetailCommand command) {
        ProductLikeView productLikeView = productService.getProductDetail(command.productId(), command.memberId());
        BrandModel brandModel = brandService.getBrandDetail(productLikeView.product().getBrandId());

        return ProductDetailResult.of(productLikeView, brandModel);
    }

}

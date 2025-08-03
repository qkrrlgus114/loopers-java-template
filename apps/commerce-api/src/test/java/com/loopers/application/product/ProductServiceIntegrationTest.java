package com.loopers.application.product;

import com.loopers.application.product.command.ProductDetailCommand;
import com.loopers.application.product.facade.ProductFacade;
import com.loopers.application.product.result.ProductDetailResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.productlike.ProductLike;
import com.loopers.domain.productlike.ProductLikeRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductLikeRepository productLikeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("상품 상세 조회를 진행할 때, ")
    class GetProductDetail {

        @DisplayName("유효한 상품 ID와 회원 ID를 주면, 상품 상세 조회에 성공한다.")
        @Test
        void successGetProductDetail() {
            // Given
            Brand brandModel = Brand.create(
                    "브랜드 이름",
                    "브랜드 설명입니다아아아"
                    , 1L
            );
            Brand brand = brandRepository.register(brandModel).get();

            Product productModel = Product.create(
                    "상품 이름",
                    "상품 설명입니다아아아아상품 설명입니다아아아아상품 설명입니다아아아아상품 설명입니다아아아아",
                    brand.getId(),
                    1L,
                    BigDecimal.valueOf(10000)
            );
            Product product = productRepository.register(productModel).get();

            ProductLike productLike = ProductLike.create(
                    product.getId(),
                    1L
            );
            productLikeRepository.register(productLike);

            ProductDetailCommand command = new ProductDetailCommand(product.getId(), 1L);


            // When
            ProductDetailResult result = productFacade.getProductDetail(command);

            // Then
            assertAll(
                    () -> assertNotNull(result)
                    , () -> assertEquals(product.getId(), result.id())
                    , () -> assertEquals(product.getName(), result.name())
                    , () -> assertEquals(product.getDescription(), result.description())
                    , () -> assertEquals(0, result.price().compareTo(product.getPrice()))
                    , () -> assertEquals(product.getMemberId(), result.memberId())
                    , () -> assertEquals(brand.getId(), result.brandId())
                    , () -> assertEquals(brand.getName(), result.brandName())
                    , () -> assertEquals(0, result.likeCount())
                    , () -> assertEquals(true, result.isLiked())
                    , () -> assertEquals(product.getStatus(), ProductStatus.REGISTERED)
            );
        }


        @DisplayName("유효하지 않은 상품 ID를 주면, 상품 상세 조회에 실패한다.")
        @Test
        void failGetProductDetail_whenInvalidProductId() {
            ProductDetailCommand command = new ProductDetailCommand(-1L, 1L);

            Assertions.assertThrows(CoreException.class, () -> {
                productFacade.getProductDetail(command);
            });
        }

        @DisplayName("유효하지 않은 회원 ID를 주면, 상품 상세 조회에 실패한다.")
        @Test
        void failGetProductDetail_whenInvalidMemberId() {
            ProductDetailCommand command = new ProductDetailCommand(1L, -1L);

            Assertions.assertThrows(CoreException.class, () -> {
                productFacade.getProductDetail(command);
            });
        }


    }


}

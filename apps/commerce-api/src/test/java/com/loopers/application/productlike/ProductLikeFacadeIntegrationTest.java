package com.loopers.application.productlike;

import com.loopers.application.productlike.command.ProductLikeCommand;
import com.loopers.application.productlike.facade.ProductLikeFacade;
import com.loopers.application.productlike.result.ProductLikeResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductLikeFacadeIntegrationTest {

    @Autowired
    private ProductLikeFacade productLikeFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    private Member setUpMember;
    private Product setUpProduct;

    @BeforeEach
    void setUp() {
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
        setUpProduct = productRepository.register(productModel).get();

        Member member = Member.registerMember(
                "testUser",
                "password123",
                "test@naver.com",
                "테스트 유저",
                LocalDate.parse("1990-01-01"),
                "M");
        setUpMember = memberRepository.register(member).get();
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("상품 좋아요를 토글할 때, ")
    class ToggleProductLike {

        @DisplayName("좋아요를 누르지 않은 상태에서 누르면, 좋아요 수가 1 증가하고, isLiked가 true로 반환된다.")
        @Test
        void successToggleProductLike_whenNotLiked() {
            ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

            ProductLikeResult result = productLikeFacade.toggleProductLike(command);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(setUpProduct.getId(), result.getProductId()),
                    () -> assertTrue(result.isLiked()),
                    () -> assertEquals(1, result.getLikeCount()),
                    () -> assertTrue(result.isStatus())
            );
        }

        @DisplayName("좋아요를 누른 상태에서 누르면, 좋아요 수에 변함이 없고, isLiked가 true로 반환된다.")
        @Test
        void successToggleProductLike_whenAlreadyLiked() {
            // 좋아요 누른 상태로 시작
            productLikeFacade.toggleProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));
            ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

            ProductLikeResult result = productLikeFacade.toggleProductLike(command);

            // Then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(setUpProduct.getId(), result.getProductId()),
                    () -> assertTrue(result.isLiked()),
                    () -> assertEquals(1, result.getLikeCount()),
                    () -> assertFalse(result.isStatus())
            );
        }
    }

    @Nested
    @DisplayName("상품 좋아요를 취소할 때, ")
    class CancelProductLike {

        @DisplayName("좋아요를 누른 상태에서 취소하면, 좋아요 수가 1 감소하고, isLiked가 false로 반환된다.")
        @Test
        void successCancelProductLike_whenLiked() {
            // Given
            productLikeFacade.toggleProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));
            ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

            // When
            ProductLikeResult result = productLikeFacade.cancelProductLike(command);

            // Then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(setUpProduct.getId(), result.getProductId()),
                    () -> assertFalse(result.isLiked()),
                    () -> assertEquals(0, result.getLikeCount()),
                    () -> assertTrue(result.isStatus())
            );
        }

        @DisplayName("좋아요를 누르지 않은 상태에서 취소하면, 좋아요 수에 변함이 없고, isLiked가 false로 반환된다.")
        @Test
        void successCancelProductLike_whenNotLiked() {
            // Given
            ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

            // When
            ProductLikeResult result = productLikeFacade.cancelProductLike(command);

            // Then
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(setUpProduct.getId(), result.getProductId()),
                    () -> assertFalse(result.isLiked()),
                    () -> assertEquals(0, result.getLikeCount()),
                    () -> assertFalse(result.isStatus())
            );
        }
    }
}

package com.loopers.application.productlike;

import com.loopers.application.productlike.command.ProductLikeCommand;
import com.loopers.application.productlike.facade.ProductLikeFacade;
import com.loopers.application.productlike.result.ProductLikeResult;
import com.loopers.application.productlike.result.ProductLikeView;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.productlike.ProductLikeRepository;
import com.loopers.utils.DatabaseCleanUp;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

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

    @Autowired
    private ProductLikeRepository productLikeRepository;

    @Autowired
    private EntityManager em;

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

            ProductLikeResult result = productLikeFacade.registerProductLike(command);

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
            productLikeFacade.registerProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));
            ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

            ProductLikeResult result = productLikeFacade.registerProductLike(command);

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
            productLikeFacade.registerProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));
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

    @DisplayName("상품 좋아요 스케줄러가 likeCount를 정상 업데이트 한다.")
    @Test
    @Transactional
    void successUpdateAllProductLikeCount() {
        // Given
        productLikeFacade.registerProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));

        // when
        productLikeFacade.updateAllProductLikeCount();

        em.flush();
        em.clear();

        // then
        Product product = productRepository.findById(setUpProduct.getId()).orElseThrow();
        assertEquals(1, product.getLikeCount());

        productLikeFacade.cancelProductLike(ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId()));

        productLikeFacade.updateAllProductLikeCount();

        em.flush();
        em.clear();

        Product updatedProduct = productRepository.findById(setUpProduct.getId()).orElseThrow();
        assertEquals(0, updatedProduct.getLikeCount());
    }

    @DisplayName("동시에 좋아요를 눌러도 멱등성이 보장된다.")
    @Test
    void successIdempotentToggleProductLike() throws InterruptedException {
        // Given
        ProductLikeCommand command = ProductLikeCommand.of(setUpProduct.getId(), setUpMember.getId());

        // When
        int threadCount = 9;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    productLikeFacade.registerProductLike(command);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        List<ProductLikeView> productLikeList = productLikeFacade.getProductLikeList(command.getMemberId());

        assertAll(
                () -> assertNotNull(productLikeList),
                () -> assertEquals(1, productLikeList.size()),
                () -> assertEquals(setUpProduct.getId(), productLikeList.get(0).getProductId()),
                () -> assertEquals(1, productLikeList.get(0).getLikeCount())
        );
    }

    @DisplayName("동일한 상품에 대해 여러명이 좋아요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다.")
    @Test
    void successConcurrentLikesOnSameProduct() throws InterruptedException {
        // Given
        List<Optional<Member>> members;
        members = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> Member.registerMember(
                        "testUser" + i,
                        "password123",
                        "test" + i + "@naver.com",
                        "테스트유저" + i,
                        LocalDate.parse("1990-01-01"),
                        "M"))
                .map(memberRepository::register)
                .toList();
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // id가 1,2,3인 3명이서 좋아요 요청을 보낸다고 가정
        for (Optional<Member> m : members) {
            IntStream.range(0, 10).forEach(i ->
                    executor.submit(() -> {
                        try {
                            ProductLikeCommand cmd =
                                    ProductLikeCommand.of(setUpProduct.getId(), m.get().getId());
                            productLikeFacade.registerProductLike(cmd);
                        } finally {
                            latch.countDown();
                        }
                    }));
        }

        latch.await();

        int count = productLikeRepository.countByProductId(setUpProduct.getId());

        assertEquals(3, count);
    }
}

package com.loopers.application.orders;

import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.facade.OrdersFacade;
import com.loopers.application.orders.result.OrdersInfoResult;
import com.loopers.application.orders.result.OrdersRegisterInfoResult;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponStatus;
import com.loopers.domain.coupon.CouponType;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
import com.loopers.domain.orders.OrderStatus;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderFacadeIntegrationTest {

    @Autowired
    private OrdersFacade ordersFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponMemberRepository couponMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    private Member setUpMember;
    private Product setUpProduct;
    private Stock setUpStock;
    private Point setUpPoint;

    @BeforeEach
    void setUp() {
        Member member = Member.registerMember(
                "testUser",
                "password123",
                "test@naver.com",
                "테스트 유저",
                LocalDate.parse("1990-01-01"),
                "M");
        setUpMember = memberRepository.register(member).get();

        Product product = Product.create(
                "테스트 상품",
                "상품 설명상품 설명상품 설명상품 설명상품 설명상품 설명상품 설명",
                1L,
                setUpMember.getId(),
                BigDecimal.valueOf(1000L)
        );
        setUpProduct = productRepository.register(product).get();

        Stock stock = Stock.create(setUpProduct.getId(), 100);
        setUpStock = stockRepository.register(stock);

        Point point = Point.create(
                setUpMember.getId(),
                BigDecimal.valueOf(10000L)
        );
        setUpPoint = pointRepository.register(point);
    }

    @DisplayName("주문을 등록할 때, ")
    @Nested
    class Register {

        @DisplayName("모든 조건이 만족되면 주문이 등록된다.")
        @Test
        void successRegister_whenAllConditionsMet() {
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(1000L))
            );
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    null,
                    PaymentType.CARD,
                    CardType.SAMSUNG,
                    "1234-1234-1234-1234"
            );

            OrdersRegisterInfoResult ordersRegisterInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            assertAll(
                    () -> assertNotNull(ordersRegisterInfoResult),
                    () -> assertEquals(OrderStatus.PENDING, ordersRegisterInfoResult.getOrderStatus())
            );
        }

        @DisplayName("정액 할인 쿠폰을 사용하여 성공적으로 주문을 등록한다. (1000원 할인)")
        @Test
        void successRegister_whenUsingFixedDiscountCoupon() {
            // Given
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(2000L))
            );
            Coupon coupon = Coupon.create(
                    "1000원 할인 쿠폰",
                    CouponType.FIXED_AMOUNT,
                    BigDecimal.valueOf(1000L),
                    null,
                    BigDecimal.valueOf(1000L),
                    30
            );
            Coupon savedCoupon = couponRepository.save(coupon);

            CouponMember couponMember = CouponMember.create(
                    setUpMember.getId(),
                    savedCoupon.getId(),
                    CouponStatus.ACTIVE,
                    LocalDateTime.now().plusDays(5),
                    null
            );
            CouponMember savedCouponMember = couponMemberRepository.save(couponMember);
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    couponMember.getCouponId(),
                    PaymentType.POINT,
                    null,
                    null
            );

            // When
            OrdersRegisterInfoResult ordersRegisterInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            // Then
            assertAll(
                    () -> assertNotNull(ordersRegisterInfoResult),
                    () -> assertEquals(OrderStatus.PENDING, ordersRegisterInfoResult.getOrderStatus()),
                    () -> assertEquals(0, ordersRegisterInfoResult.getTotalPrice().compareTo(BigDecimal.valueOf(3000L)))
            );
        }

        @DisplayName("정률 할인 쿠폰을 사용하여 성공적으로 주문을 등록한다. (10% 할인)")
        @Test
        void successRegister_whenUsingPercentageDiscountCoupon() {
            // Given
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(2000L))
            );
            Coupon coupon = Coupon.create(
                    "10% 할인 쿠폰",
                    CouponType.PERCENTAGE,
                    null,
                    10,
                    BigDecimal.valueOf(1000L),
                    30
            );
            Coupon savedCoupon = couponRepository.save(coupon);

            CouponMember couponMember = CouponMember.create(
                    setUpMember.getId(),
                    savedCoupon.getId(),
                    CouponStatus.ACTIVE,
                    LocalDateTime.now().plusDays(5),
                    null
            );
            CouponMember savedCouponMember = couponMemberRepository.save(couponMember);
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    couponMember.getCouponId(),
                    PaymentType.POINT,
                    null,
                    null
            );

            // When
            OrdersRegisterInfoResult ordersRegisterInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            // Then
            assertAll(
                    () -> assertNotNull(ordersRegisterInfoResult),
                    () -> assertEquals(OrderStatus.PENDING, ordersRegisterInfoResult.getOrderStatus()),
                    () -> assertEquals(0, ordersRegisterInfoResult.getTotalPrice().compareTo(BigDecimal.valueOf(3600L)))
            );
        }

        @DisplayName("이미 사용된 쿠폰을 사용하려고 하면 주문에 실패한다.")
        @Test
        void failRegister_whenCouponAlreadyUsed() {
            // Given
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(2000L))
            );
            Coupon coupon = Coupon.create(
                    "1000원 할인 쿠폰",
                    CouponType.FIXED_AMOUNT,
                    BigDecimal.valueOf(1000L),
                    null,
                    BigDecimal.valueOf(1000L),
                    30
            );
            Coupon savedCoupon = couponRepository.save(coupon);

            CouponMember couponMember = CouponMember.create(
                    setUpMember.getId(),
                    savedCoupon.getId(),
                    CouponStatus.USED,
                    LocalDateTime.now().plusDays(5),
                    null
            );
            couponMemberRepository.save(couponMember);
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    couponMember.getCouponId(),
                    PaymentType.POINT,
                    null,
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }

        @DisplayName("동시에 주문을 등록할 때, ")
        @Nested
        class ConcurrentOrderRegistration {

            @DisplayName("재고가 부족하면 주문에 실패한다.")
            @Test
            void failRegister_whenConcurrentOrdersWithInsufficientStock() throws InterruptedException {
                // Given
                List<PlaceOrderCommand.Item> items = List.of(
                        new PlaceOrderCommand.Item(setUpProduct.getId(), 101, BigDecimal.valueOf(1000L))
                );
                PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                        setUpMember.getId(),
                        items,
                        null,
                        PaymentType.POINT,
                        null,
                        null
                );

                int threadCount = 10;
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch latch = new CountDownLatch(threadCount);

                // when
                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            ordersFacade.placeOrder(placeOrderCommand);
                        } catch (CoreException e) {
                            // 예외 처리 로직 필요
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await();
                List<OrdersInfoResult> ordersByMemberId = ordersFacade.getOrdersByMemberId(setUpMember.getId());


                // then
                Assertions.assertThrows(CoreException.class, () -> {
                    ordersFacade.placeOrder(placeOrderCommand);
                });
                assertAll(
                        () -> assertNotNull(ordersByMemberId),
                        () -> Assertions.assertTrue(ordersByMemberId.isEmpty())
                );
            }

            @DisplayName("쿠폰은 한 번만 사용된다.")
            @Test
            void successRegister_whenConcurrentOrdersWithSameCoupon() throws InterruptedException {
                // Given
                List<PlaceOrderCommand.Item> items = List.of(
                        new PlaceOrderCommand.Item(setUpProduct.getId(), 1, BigDecimal.valueOf(2000L))
                );
                Coupon coupon = Coupon.create(
                        "1000원 할인 쿠폰",
                        CouponType.FIXED_AMOUNT,
                        BigDecimal.valueOf(1000L),
                        null,
                        BigDecimal.valueOf(1000L),
                        30
                );
                Coupon savedCoupon = couponRepository.save(coupon);

                CouponMember couponMember = CouponMember.create(
                        setUpMember.getId(),
                        savedCoupon.getId(),
                        CouponStatus.ACTIVE,
                        LocalDateTime.now().plusDays(5),
                        null
                );
                couponMemberRepository.save(couponMember);
                PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                        setUpMember.getId(),
                        items,
                        couponMember.getCouponId(),
                        PaymentType.POINT,
                        null,
                        null
                );

                int threadCount = 10;
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch latch = new CountDownLatch(threadCount);

                // when
                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            ordersFacade.placeOrder(placeOrderCommand);
                        } catch (CoreException e) {
                            // 예외 처리 로직 필요
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await();

                // then
                List<OrdersInfoResult> ordersList = ordersFacade.getOrdersByMemberId(setUpMember.getId());

                assertAll(
                        () -> assertNotNull(ordersList),
                        () -> assertEquals(1, ordersList.size())
                );
            }

            @DisplayName("동일한 유저가 서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다.")
            @Test
            void successRegister_whenConcurrentOrdersWithSameUser() throws InterruptedException {
                // Given
                List<PlaceOrderCommand.Item> items1 = List.of(
                        new PlaceOrderCommand.Item(setUpProduct.getId(), 1, BigDecimal.valueOf(500L))
                );
                List<PlaceOrderCommand.Item> items2 = List.of(
                        new PlaceOrderCommand.Item(setUpProduct.getId(), 1, BigDecimal.valueOf(600L))
                );
                PlaceOrderCommand placeOrderCommand1 = PlaceOrderCommand.of(
                        setUpMember.getId(),
                        items1,
                        null,
                        PaymentType.POINT,
                        null,
                        null
                );
                PlaceOrderCommand placeOrderCommand2 = PlaceOrderCommand.of(
                        setUpMember.getId(),
                        items2,
                        null,
                        PaymentType.POINT,
                        null,
                        null
                );

                int threadCount = 10;
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch latch = new CountDownLatch(threadCount);

                // when
                for (int i = 0; i < threadCount; i++) {
                    if (i % 2 == 0) {
                        executor.submit(() -> {
                            try {
                                ordersFacade.placeOrder(placeOrderCommand1);
                            } catch (CoreException ignored) {
                            } finally {
                                latch.countDown();
                            }
                        });
                    } else {
                        executor.submit(() -> {
                            try {
                                ordersFacade.placeOrder(placeOrderCommand2);
                            } catch (CoreException ignored) {
                            } finally {
                                latch.countDown();
                            }
                        });
                    }
                }

                latch.await();

                // then
                List<OrdersInfoResult> ordersList = ordersFacade.getOrdersByMemberId(setUpMember.getId());
                Point point = pointRepository.findByMemberId(setUpMember.getId()).get();

                assertAll(
                        () -> assertNotNull(ordersList),
                        () -> assertEquals(10, ordersList.size()),
                        () -> assertEquals(0, point.getAmount().compareTo(BigDecimal.valueOf(4500)))
                );
            }

            @DisplayName("동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다. ")
            @Test
            void successRegister_whenConcurrentOrdersWithSameProduct() throws InterruptedException {
                // Given
                List<PlaceOrderCommand.Item> items = List.of(
                        new PlaceOrderCommand.Item(setUpProduct.getId(), 1, BigDecimal.valueOf(1000L))
                );
                PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                        setUpMember.getId(),
                        items,
                        null,
                        PaymentType.POINT,
                        null,
                        null
                );

                int threadCount = 10;
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch latch = new CountDownLatch(threadCount);

                // when
                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            ordersFacade.placeOrder(placeOrderCommand);
                        } catch (CoreException ignored) {
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await();

                // then
                Stock stock = stockRepository.findByProductId(setUpProduct.getId());
                assertAll(
                        () -> assertNotNull(stock),
                        () -> assertEquals(90, stock.getQuantity())
                );
            }
        }

        @DisplayName("사용 불가능한 쿠폰을 사용하려고 하면 주문에 실패한다.")
        @Test
        void failRegister_whenUsingInvalidCoupon() {
            // Given
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(2000L))
            );
            Coupon coupon = Coupon.create(
                    "사용 불가능한 쿠폰",
                    CouponType.FIXED_AMOUNT,
                    BigDecimal.valueOf(1000L),
                    null,
                    BigDecimal.valueOf(1000L),
                    30
            );
            Coupon savedCoupon = couponRepository.save(coupon);

            CouponMember couponMember = CouponMember.create(
                    setUpMember.getId(),
                    savedCoupon.getId(),
                    CouponStatus.EXPIRED,
                    LocalDateTime.now().plusDays(5),
                    null
            );
            couponMemberRepository.save(couponMember);
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    couponMember.getCouponId(),
                    PaymentType.POINT,
                    null,
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }

        @DisplayName("존재하지 않는 쿠폰으로 주문을 등록하려고 하면 주문에 실패한다.")
        @Test
        void failRegister_whenUsingNonExistentCoupon() {
            // Given
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(2000L))
            );
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    9999L, // 존재하지 않는 쿠폰 ID
                    PaymentType.POINT,
                    null,
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }

        @DisplayName("주문이 등록되면 포인트와 재고가 차감된다.")
        @Test
        void successRegister_thenPointAndStockAreReduced() {
            // Given
            BigDecimal initialPoint = setUpPoint.getAmount();
            int initialStock = setUpStock.getQuantity();
            int quantity1 = 2;
            int price1 = 1000;
            int quantity2 = 2;
            int price2 = 1000;

            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 4, BigDecimal.valueOf(1000L))
            );
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    null,
                    PaymentType.POINT,
                    null,
                    null
            );

            // When
            ordersFacade.placeOrder(placeOrderCommand);

            // Then
            Point updatedPoint = pointRepository.findById(setUpPoint.getId()).get();
            Stock updatedStock = stockRepository.findById(setUpStock.getId());

            BigDecimal totalCost = BigDecimal.valueOf((long) quantity1 * price1 + (long) quantity2 * price2);
            int totalQuantity = quantity1 + quantity2;

            assertAll(
                    () -> assertEquals(0, initialPoint.subtract(totalCost).compareTo(updatedPoint.getAmount())),
                    () -> assertEquals(initialStock - totalQuantity, updatedStock.getQuantity())
            );
        }

        @DisplayName("사용자의 포인트가 부족하면 주문에 실패한다.")
        @Test
        void failRegister_whenPointNotEnough() {
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(100000L))
            );
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    null,
                    PaymentType.POINT,
                    null,
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }

        @DisplayName("상품의 재고가 부족하면 주문에 실패한다.")
        @Test
        void failRegister_whenStockNotEnough() {
            List<PlaceOrderCommand.Item> items = List.of(
                    new PlaceOrderCommand.Item(setUpProduct.getId(), 101, BigDecimal.valueOf(1000L))
            );
            PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                    setUpMember.getId(),
                    items,
                    null,
                    PaymentType.POINT,
                    null,
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }
    }

    @DisplayName("사용자의 모든 주문 내역 조회를 성공한다.")
    @Test
    void successGetAllOrdersByMember() {
        // Given
        List<PlaceOrderCommand.Item> items = List.of(
                new PlaceOrderCommand.Item(setUpProduct.getId(), 2, BigDecimal.valueOf(1000L))
        );
        PlaceOrderCommand placeOrderCommand = PlaceOrderCommand.of(
                setUpMember.getId(),
                items,
                null,
                PaymentType.POINT,
                null,
                null
        );

        // When
        ordersFacade.placeOrder(placeOrderCommand);

        // Then
        List<OrdersInfoResult> ordersByMemberId = ordersFacade.getOrdersByMemberId(setUpMember.getId());

        assertAll(
                () -> assertNotNull(ordersByMemberId),
                () -> Assertions.assertFalse(ordersByMemberId.isEmpty()),
                () -> assertEquals(1, ordersByMemberId.size())
        );
    }


}

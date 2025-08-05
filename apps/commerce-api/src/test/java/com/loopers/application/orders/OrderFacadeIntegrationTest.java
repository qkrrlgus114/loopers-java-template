package com.loopers.application.orders;

import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.facade.OrdersFacade;
import com.loopers.application.orders.result.OrdersInfoResult;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponStatus;
import com.loopers.domain.coupon.CouponType;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.couponmember.CouponMemberRepository;
import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
import com.loopers.domain.orders.OrderStatus;
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

import static org.junit.jupiter.api.Assertions.assertAll;


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
                    null
            );

            OrdersInfoResult ordersInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            assertAll(
                    () -> Assertions.assertNotNull(ordersInfoResult),
                    () -> Assertions.assertEquals(OrderStatus.PENDING, ordersInfoResult.getStatus())
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
                    couponMember.getCouponId()
            );

            // When
            OrdersInfoResult ordersInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            // Then
            assertAll(
                    () -> Assertions.assertNotNull(ordersInfoResult),
                    () -> Assertions.assertEquals(OrderStatus.PENDING, ordersInfoResult.getStatus()),
                    () -> Assertions.assertEquals(0, ordersInfoResult.getTotalPrice().compareTo(BigDecimal.valueOf(3000L)))
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
                    couponMember.getCouponId()
            );

            // When
            OrdersInfoResult ordersInfoResult = ordersFacade.placeOrder(placeOrderCommand);

            // Then
            assertAll(
                    () -> Assertions.assertNotNull(ordersInfoResult),
                    () -> Assertions.assertEquals(OrderStatus.PENDING, ordersInfoResult.getStatus()),
                    () -> Assertions.assertEquals(0, ordersInfoResult.getTotalPrice().compareTo(BigDecimal.valueOf(3600L)))
            );
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
                    null
            );

            // When
            ordersFacade.placeOrder(placeOrderCommand);

            // Then
            Point updatedPoint = pointRepository.findById(setUpPoint.getId());
            Stock updatedStock = stockRepository.findById(setUpStock.getId());

            BigDecimal totalCost = BigDecimal.valueOf((long) quantity1 * price1 + (long) quantity2 * price2);
            int totalQuantity = quantity1 + quantity2;

            assertAll(
                    () -> Assertions.assertEquals(0, initialPoint.subtract(totalCost).compareTo(updatedPoint.getAmount())),
                    () -> Assertions.assertEquals(initialStock - totalQuantity, updatedStock.getQuantity())
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
                    null
            );

            // When & Then
            Assertions.assertThrows(CoreException.class, () -> {
                ordersFacade.placeOrder(placeOrderCommand);
            });
        }
    }


}

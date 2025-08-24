package com.loopers.application.orders.facade;

import com.loopers.application.coupon.CouponService;
import com.loopers.application.couponmember.CouponMemberService;
import com.loopers.application.member.service.MemberService;
import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.result.OrdersInfoResult;
import com.loopers.application.orders.result.OrdersRegisterInfoResult;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.application.payment.PaymentContext;
import com.loopers.application.payment.PaymentService;
import com.loopers.application.payment.processor.PaymentProcessorRegistry;
import com.loopers.application.payment.processor.PaymentResult;
import com.loopers.application.product.service.ProductService;
import com.loopers.application.stock.service.StockService;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.member.Member;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemDomainService;
import com.loopers.domain.orders.OrderStatus;
import com.loopers.domain.orders.Orders;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.product.Product;
import com.loopers.domain.stock.Stock;
import com.loopers.support.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersFacade {

    private final OrdersService ordersService;
    private final ProductService productService;
    private final OrderItemDomainService orderItemDomainService;
    private final OrderItemService orderItemsService;
    private final StockService stockService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final CouponMemberService couponMemberService;
    private final PaymentProcessorRegistry paymentProcessorRegistry;
    private final PaymentService paymentService;

    /*
     * 주문하기
     *
     * 1. 사용자 조회
     * 2. 상품 조회
     * 3. 상품 재고 확인
     * 4. 상품 재고 감소
     * 4. 사용자 쿠폰 확인
     * 5. 사용자 쿠폰 사용처리
     * 5. 사용자 포인트 확인
     * 6. 주문 생성
     * */
    @Transactional
    public OrdersRegisterInfoResult placeOrder(PlaceOrderCommand command) {
        // 주문 키 생성
        String orderKey = UUIDUtil.generateShortUUID();

        // 1. 사용자 조회
        Member member = memberService.findMemberById(command.getMemberId());

        BigDecimal totalPrice = BigDecimal.ZERO;
        int quantity = 0;

        // 2. 상품 조회
        for (PlaceOrderCommand.Item item : command.getItems()) {
            Product product = productService.findProductById(item.getProductId());

            // 3. 상품 재고 확인
            Stock stock = stockService.findStockByProductId(product.getId());
            stock.decreaseQuantity(item.getQuantity());

            // 총 금액 계산
            totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            quantity += item.getQuantity();
        }

        // 쿠폰 할인 계산
        BigDecimal couponDiscount = totalPrice;
        if (command.getCouponId() != null) {
            // 5. 사용자 쿠폰 확인
            CouponMember couponMember = couponMemberService.getCouponMemberById(member.getId(), command.getCouponId());
            Coupon coupon = couponService.getCouponId(command.getCouponId());

            couponDiscount = coupon.calculateDiscount(totalPrice);

            couponMember.useCoupon(); // 쿠폰 사용 처리
        }

        // 주문 생성
        Orders orders = ordersService.register(
                member.getId(),
                quantity,
                couponDiscount,
                command.getCouponId(),
                command.getCouponId() != null,
                orderKey
        );

        // 결제 생성
        Payment payment = paymentService.register(
                orders.getId(),
                orders.getOrderKey(),
                command.getPaymentType(),
                command.getCardType(),
                command.getCardNo(),
                couponDiscount,
                member.getId()
        );

        // 결제로 보낼 context 생성
        PaymentContext paymentContext = PaymentContext.of(
                orderKey,
                member.getId(),
                couponDiscount,
                command.getPaymentType(),
                command.getCardType(),
                command.getCardNo(),
                payment.getId()
        );

        // 주문 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (PlaceOrderCommand.Item item : command.getItems()) {
            OrderItem orderItem = orderItemDomainService.createOrderItem(
                    orders.getId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice()
            );
            orderItems.add(orderItem);
        }
        orderItemsService.register(orderItems);

        // 결제 요청 처리(타입에 따라 전략 이용)
        PaymentResult paymentResult = paymentProcessorRegistry.get(command.getPaymentType()).processPayment(paymentContext);

        return OrdersRegisterInfoResult.of(
                OrderStatus.PENDING,
                paymentResult.status(),
                LocalDateTime.now(),
                couponDiscount,
                orderItems.size()
        );
    }

    /*
     * 사용자의 전체 주문 리스트 조회하기
     * */
    @Transactional(readOnly = true)
    public List<OrdersInfoResult> getOrdersByMemberId(Long memberId) {
        List<Orders> ordersList = ordersService.findAllByMemberId(memberId);

        if (ordersList == null || ordersList.isEmpty()) {
            return new ArrayList<>();
        }

        List<OrdersInfoResult> ordersInfoResults = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersInfoResult result = OrdersInfoResult.of(
                    orders.getId(),
                    orders.getOrderStatus(),
                    orders.getQuantity(),
                    orders.getTotalPrice(),
                    orders.getCouponMemberId()
            );
            ordersInfoResults.add(result);
        }

        return ordersInfoResults;
    }
}


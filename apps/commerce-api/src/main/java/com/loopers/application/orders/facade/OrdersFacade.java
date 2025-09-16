package com.loopers.application.orders.facade;

import com.loopers.application.coupon.CouponService;
import com.loopers.application.member.service.MemberService;
import com.loopers.application.orderItem.service.OrderItemService;
import com.loopers.application.orders.command.PlaceOrderCommand;
import com.loopers.application.orders.result.OrdersInfoResult;
import com.loopers.application.orders.result.OrdersRegisterInfoResult;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.member.Member;
import com.loopers.domain.orderItem.OrderItem;
import com.loopers.domain.orderItem.OrderItemDomainService;
import com.loopers.domain.orders.Orders;
import com.loopers.domain.orders.event.OrdersCreatedEvent;
import com.loopers.support.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final OrderItemDomainService orderItemDomainService;
    private final OrderItemService orderItemsService;
    private final MemberService memberService;
    private final CouponService couponService;

    private final ApplicationEventPublisher eventPublisher;

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

        try {
            // 1. 사용자 조회
            Member member = memberService.findMemberById(command.getMemberId());

            BigDecimal totalPrice = BigDecimal.ZERO;
            int quantity = 0;

            for (PlaceOrderCommand.Item item : command.getItems()) {
                quantity += item.getQuantity();
                totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            // 쿠폰 할인 계산
            BigDecimal finalPrice = totalPrice;
            if (command.getCouponId() != null) {
                Coupon coupon = couponService.getCouponId(command.getCouponId());
                finalPrice = coupon.calculateDiscount(totalPrice);
            }

            // 주문 생성
            Orders orders = ordersService.register(
                    member.getId(),
                    quantity,
                    finalPrice,
                    orderKey
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

            // 이벤트 발행
            OrdersCreatedEvent event = OrdersCreatedEvent.of(
                    orders.getId(),
                    orderKey,
                    member.getId(),
                    command.getCouponId(),
                    command.getPaymentType(),
                    command.getCardType(),
                    command.getCardNo()
            );
            eventPublisher.publishEvent(event);


            OrdersRegisterInfoResult result = OrdersRegisterInfoResult.of(
                    orders.getId(),
                    orders.getOrderStatus(),
                    LocalDateTime.from(orders.getCreatedAt()),
                    orders.getTotalPrice(),
                    orders.getQuantity()
            );
            return result;

        } catch (Exception e) {
            throw e;
        }
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


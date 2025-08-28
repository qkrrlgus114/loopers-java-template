package com.loopers.application.orders.listener;

import com.loopers.application.couponmember.CouponMemberService;
import com.loopers.application.orders.service.OrderFailService;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.orders.event.CouponProcessedEvent;
import com.loopers.domain.orders.event.OrdersCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CouponEventListener {

    private final CouponMemberService couponMemberService;
    private final OrdersService ordersService;
    private final OrderFailService orderFailService;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrdersCreatedEvent event) {
        try {
            if (event.couponId() != null) {
                CouponMember couponMember = couponMemberService.getCouponMemberById(event.memberId(), event.couponId());
                couponMember.useCoupon();
            }

            // 다음 이벤트를 발행하여 체인을 이어갑니다.
            eventPublisher.publishEvent(CouponProcessedEvent.from(event));
        } catch (Exception e) {
            orderFailService.markFailed(event.ordersId());
            throw e;
        }
    }
}

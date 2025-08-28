package com.loopers.application.orders.listener;

import com.loopers.application.couponmember.CouponMemberService;
import com.loopers.application.orders.service.OrderFailService;
import com.loopers.application.orders.service.OrdersService;
import com.loopers.domain.couponmember.CouponMember;
import com.loopers.domain.orders.event.CouponProcessedEvent;
import com.loopers.domain.orders.event.OrdersCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
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
        String orderKey = event.orderKey();
        log.info("[{}] 쿠폰 처리 리스너 시작. event: {}", orderKey, event);

        try {
            if (event.couponId() != null) {
                log.info("[{}] 쿠폰 사용 처리 시작. memberId: {}, couponId: {}", orderKey, event.memberId(), event.couponId());
                CouponMember couponMember = couponMemberService.getCouponMemberById(event.memberId(), event.couponId());
                couponMember.useCoupon();
                log.info("[{}] 쿠폰 사용 처리 완료.", orderKey);
            } else {
                log.info("[{}] 사용된 쿠폰 없음.", orderKey);
            }

            // 다음 이벤트를 발행하여 체인을 이어갑니다.
            CouponProcessedEvent nextEvent = CouponProcessedEvent.from(event);
            log.info("[{}] CouponProcessedEvent 발행 시작. event: {}", orderKey, nextEvent);
            eventPublisher.publishEvent(nextEvent);
            log.info("[{}] CouponProcessedEvent 발행 완료.", orderKey);

        } catch (Exception e) {
            log.error("[{}] 쿠폰 처리 실패. event: {}", orderKey, event, e);
            orderFailService.markFailed(event.ordersId());
            throw e;
        }
    }
}

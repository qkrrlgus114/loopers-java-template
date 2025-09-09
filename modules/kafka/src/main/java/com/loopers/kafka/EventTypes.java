package com.loopers.kafka;

public class EventTypes {
    // 좋아요 이벤트
    public static final String PRODUCT_LIKED_EVENT = "ProductLikedEvent";
    public static final String PRODUCT_UNLIKED_EVENT = "ProductUnlikedEvent";

    // 재고 이벤트
    public static final String STOCK_CHANGED = "Changed";

    // 주문 이벤트
    public static final String ORDER_CREATED = "Created";
    public static final String ORDER_CONFIRMED = "Confirmed";
    public static final String ORDER_CANCELLED = "Cancelled";

    // 결제 이벤트
    public static final String PAYMENT_COMPLETED = "Completed";
    public static final String PAYMENT_FAILED = "Failed";

    // 상세 페이지 조회
    public static final String PRODUCT_DETAIL_VIEWED_EVENT = "ProductDetailViewedEvent";

    private EventTypes() {
        throw new IllegalStateException("Constants class");
    }
}

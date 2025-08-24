package com.loopers.application.orders.command;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentType;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceOrderCommand {

    private Long memberId;
    private List<Item> items;
    private Long couponId;
    private PaymentType paymentType;
    private CardType cardType;
    private String cardNo;

    private PlaceOrderCommand(Long memberId, List<Item> items, Long couponId, PaymentType paymentType, CardType cardType, String cardNo) {
        this.memberId = memberId;
        this.items = items;
        this.couponId = couponId;
        this.paymentType = paymentType;
        this.cardType = cardType;
        this.cardNo = cardNo;
    }

    public static PlaceOrderCommand of(Long memberId, List<Item> items, Long couponId, PaymentType paymentType, CardType cardType, String cardNo) {
        validate(memberId, items, couponId, paymentType, cardType, cardNo);

        return new PlaceOrderCommand(memberId, items, couponId, paymentType, cardType, cardNo);
    }

    private static void validate(Long memberId, List<Item> items, Long couponId, PaymentType paymentType, CardType cardType, String cardNo) {
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 회원 ID입니다.");
        }
        if (items == null || items.isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "주문 항목이 비어 있습니다.");
        }
        if (couponId != null && couponId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 쿠폰 ID입니다.");
        }
        if (paymentType == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "결제 유형이 지정되지 않았습니다.");
        }
        if (!PaymentType.isValid(paymentType)) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 결제 유형입니다.");
        }
        if (paymentType == PaymentType.CARD) {
            if (cardType != null && !CardType.isValid(cardType)) {
                throw new CoreException(CommonErrorType.BAD_REQUEST, "지원하지 않는 카드 유형입니다.");
            }
            if (cardType != null && (cardNo == null || cardNo.isBlank() || !cardNo.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))) {
                throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 카드 번호입니다. 형식은 xxxx-xxxx-xxxx-xxxx 입니다.");
            }
        }
        items.forEach(PlaceOrderCommand::itemValidate);
    }

    public static void itemValidate(Item item) {
        if (item.getProductId() == null || item.getProductId() <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }
        if (item.getQuantity() <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "가격은 0 이상이어야 합니다.");
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Item {

        private Long productId;

        private int quantity;

        private BigDecimal price;

        public Item(Long productId, int quantity, BigDecimal price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public static Item of(Long productId, int quantity, BigDecimal price) {
            return new Item(productId, quantity, price);
        }
    }

}

package com.loopers.domain.payment;

public enum CardType {
    SAMSUNG, KB, HYUNDAI;

    public static boolean isValid(CardType cardType) {
        return cardType == SAMSUNG || cardType == KB || cardType == HYUNDAI;
    }
}

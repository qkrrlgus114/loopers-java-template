package com.loopers.domain.point.history;

import lombok.Getter;

@Getter
public enum PointHistoryStatus {
    CHARGED("충전"),
    EARNED("적립"),
    USED("사용"),
    EXPIRED("만료");

    private final String description;

    PointHistoryStatus(String description) {
        this.description = description;
    }

}

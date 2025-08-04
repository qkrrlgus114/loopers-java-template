package com.loopers.application.point.result;

import com.loopers.application.member.result.MemberInfoResult;
import com.loopers.domain.point.Point;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PointInfoResult {

    private Long memberId;
    private String loginId;
    private BigDecimal amount;

    public static PointInfoResult of(MemberInfoResult memberInfo, Point point) {
        return PointInfoResult.builder()
                .memberId(memberInfo.id())
                .loginId(memberInfo.loginId())
                .amount(point.getAmount())
                .build();
    }
}

package com.loopers.interfaces.api.member.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointChargeReqDTO {

    private String memberId;

    private Long amount;

}

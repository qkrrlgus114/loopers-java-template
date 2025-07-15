package com.loopers.interfaces.api.member.dto.response;

import lombok.*;

/*
 * 사용자의 포인트 정보 응답 DTO
 * */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberPointResDTO {

    private String memberId;

    private String point;

    public static MemberPointResDTO from(String memberId, String point) {
        return MemberPointResDTO.builder()
                .memberId(memberId)
                .point(point)
                .build();
    }
}

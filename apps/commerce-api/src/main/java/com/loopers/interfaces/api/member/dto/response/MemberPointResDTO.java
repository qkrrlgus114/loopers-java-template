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

    private Long point;

    public static MemberPointResDTO from(Long memberId, Long point) {
        return MemberPointResDTO.builder()
                .memberId(String.valueOf(memberId))
                .point(point)
                .build();
    }
}

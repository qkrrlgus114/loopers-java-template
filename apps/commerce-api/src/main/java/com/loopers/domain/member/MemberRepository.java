package com.loopers.domain.member;

import java.util.Optional;

/**
 * MemberModel에서 사용하는 DB 로직 추상 메서드를 정의합니다.
 */
public interface MemberRepository {

    Optional<Member> register(Member member);

    Optional<Member> findById(Long memberId);

}

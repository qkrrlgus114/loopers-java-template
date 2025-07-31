package com.loopers.infrastructure.member;

import com.loopers.domain.member.Member;
import com.loopers.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> register(Member member) {
        try {
            Member savedMember = memberJpaRepository.save(member);
            return Optional.of(savedMember);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

}

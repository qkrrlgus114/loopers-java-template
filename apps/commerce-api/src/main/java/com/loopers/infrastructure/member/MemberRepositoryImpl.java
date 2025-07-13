package com.loopers.infrastructure.member;

import com.loopers.domain.member.MemberModel;
import com.loopers.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<MemberModel> register(MemberModel memberModel) {
        try {
            MemberModel savedMember = memberJpaRepository.save(memberModel);
            return Optional.of(savedMember);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

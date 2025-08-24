package com.loopers.infrastructure.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<Point> findByMemberIdWithLock(Long memberId) {
        return pointJpaRepository.findByMemberIdWitkLock(memberId);
    }

    @Override
    public Optional<Point> findByMemberId(Long memberId) {
        return pointJpaRepository.findByMemberId(memberId);
    }

    @Override
    public Point register(Point point) {
        return pointJpaRepository.save(point);
    }

    @Override
    public Optional<Point> findById(Long id) {
        return pointJpaRepository.findById(id);
    }
}

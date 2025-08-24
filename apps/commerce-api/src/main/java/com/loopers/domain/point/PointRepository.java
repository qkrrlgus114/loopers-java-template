package com.loopers.domain.point;

import java.util.Optional;

public interface PointRepository {

    Optional<Point> findByMemberIdWithLock(Long memberId);

    Optional<Point> findByMemberId(Long memberId);

    Point register(Point point);

    Optional<Point> findById(Long id);
}

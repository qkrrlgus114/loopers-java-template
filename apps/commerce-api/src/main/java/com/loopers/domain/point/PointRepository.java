package com.loopers.domain.point;

public interface PointRepository {
    Point findByMemberId(Long memberId);

    Point register(Point point);

    Point findById(Long id);
}

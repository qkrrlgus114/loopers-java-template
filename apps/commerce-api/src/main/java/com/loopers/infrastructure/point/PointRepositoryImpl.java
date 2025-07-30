package com.loopers.infrastructure.point;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point findByMemberId(Long memberId) {
        return pointJpaRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "사용자의 포인트를 찾을 수 없습니다. memberId: " + memberId));
    }

    @Override
    public Point register(Point point) {
        return pointJpaRepository.save(point);
    }
}

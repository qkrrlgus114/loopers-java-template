package com.loopers.application.point.service;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point getPointByMemberIdWithLock(Long memberId) {
        return pointRepository.findByMemberIdWithLock(memberId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "사용자의 포인트를 찾을 수 없습니다. memberId: " + memberId));
    }

    public Point getPointByMemberId(Long memberId) {
        return pointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CoreException(CommonErrorType.NOT_FOUND, "사용자의 포인트를 찾을 수 없습니다. memberId: " + memberId));
    }

    public void register(Long id) {
        Point point = Point.initCreate(id);
        pointRepository.register(point);
    }
}

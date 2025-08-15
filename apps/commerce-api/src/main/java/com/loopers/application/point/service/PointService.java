package com.loopers.application.point.service;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point getPointByMemberIdWithLock(Long memberId) {
        return pointRepository.findByMemberIdWithLock(memberId);
    }

    public Point getPointByMemberId(Long memberId) {
        return pointRepository.findByMemberId(memberId);
    }

    public void register(Long id) {
        Point point = Point.initCreate(id);
        pointRepository.register(point);
    }
}

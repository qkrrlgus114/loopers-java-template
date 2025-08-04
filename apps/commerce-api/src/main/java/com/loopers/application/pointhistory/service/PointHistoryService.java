package com.loopers.application.pointhistory.service;

import com.loopers.domain.point.history.PointHistory;
import com.loopers.domain.point.history.PointHistoryRepository;
import com.loopers.domain.point.history.PointHistoryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    /*
     * 포인트 이력 저장
     * */
    public void savePointHistory(Long memberId, Long pointId, BigDecimal amount, PointHistoryStatus status) {
        pointHistoryRepository.save(
                PointHistory.create(memberId, pointId, amount, status)
        );
    }
}

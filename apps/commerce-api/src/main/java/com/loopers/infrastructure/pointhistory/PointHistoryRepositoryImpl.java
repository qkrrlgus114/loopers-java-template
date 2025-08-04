package com.loopers.infrastructure.pointhistory;

import com.loopers.domain.point.history.PointHistory;
import com.loopers.domain.point.history.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public void save(PointHistory pointHistory) {
        pointHistoryJpaRepository.save(pointHistory);
    }
}

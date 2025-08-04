package com.loopers.infrastructure.pointhistory;

import com.loopers.domain.point.history.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}

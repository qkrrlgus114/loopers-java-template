package com.loopers.domain.point.history;

import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

    void save(PointHistory pointHistory);
    
}

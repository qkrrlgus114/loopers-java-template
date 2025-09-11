package com.loopers.infrastructure.metric;

import com.loopers.domain.ProductDailyMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDailyMetricJpaRepository extends JpaRepository<ProductDailyMetric, Long> {
}

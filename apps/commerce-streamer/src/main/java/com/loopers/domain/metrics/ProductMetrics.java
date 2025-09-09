package com.loopers.domain.metrics;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 상품 일별 집계 데이터
 */
@Entity
@Table(
        name = "product_metrics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_metrics",
                        columnNames = {"product_id", "metric_date"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ProductMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private LocalDate metricDate;

    @Column(nullable = false)
    @Builder.Default
    private Long likeCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long orderCount = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long salesQuantity = 0L;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 비즈니스 메서드
    public void addLike() {
        this.likeCount++;
    }

    public void removeLike() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void addOrder(Long quantity) {
        this.orderCount++;
        this.salesQuantity += quantity;
    }
}

package com.loopers.infrastructure.point;

import com.loopers.domain.point.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.memberId = :memberId")
    Optional<Point> findByMemberIdWitkLock(Long memberId);

    Optional<Point> findByMemberId(Long memberId);

}

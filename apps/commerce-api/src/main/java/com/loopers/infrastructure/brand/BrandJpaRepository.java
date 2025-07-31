package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {

    /**
     * 브랜드 이름으로 브랜드를 조회합니다.
     *
     * @param name 조회할 브랜드 이름
     * @return 조회된 브랜드 모델, 없으면 null
     */
    Optional<Brand> findByName(String name);
}

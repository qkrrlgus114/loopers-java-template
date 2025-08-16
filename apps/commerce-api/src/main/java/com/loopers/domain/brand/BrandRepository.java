package com.loopers.domain.brand;

import java.util.List;
import java.util.Optional;

/**
 * BrandModel에서 사용하는 DB 로직 추상 메서드를 정의합니다.
 */
public interface BrandRepository {

    /**
     * 브랜드를 ID로 조회합니다.
     */
    Optional<Brand> findById(Long id);

    /**
     * 브랜드를 등록합니다.
     */
    Optional<Brand> register(Brand brand);


    List<Brand> findAll();
}

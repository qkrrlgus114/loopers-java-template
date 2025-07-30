package com.loopers.domain.brand;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BrandModel에서 사용하는 DB 로직 추상 메서드를 정의합니다.
 */
@Repository
public interface BrandRepository {

    /**
     * 브랜드를 ID로 조회합니다.
     * <p>
     * * @param id 조회할 브랜드 ID
     * * @return 조회된 브랜드 모델, 없으면 null
     */
    Optional<Brand> findById(Long id);

    /**
     * 브랜드를 등록합니다.
     *
     * @param brand 등록할 브랜드 모델
     * @return 등록된 브랜드 모델
     */
    Optional<Brand> register(Brand brand);


    List<Brand> findAll();
}

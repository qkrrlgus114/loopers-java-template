package com.loopers.application.brand.service;

import com.loopers.application.brand.command.BrandRegisterCommand;
import com.loopers.application.brand.command.BrandUpdateCommand;
import com.loopers.application.brand.result.BrandListResult;
import com.loopers.application.brand.result.BrandRegisterResult;
import com.loopers.application.brand.result.BrandUpdateResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.support.error.BrandErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final BrandRepository brandRepository;

    /*
     * 브랜드를 등록한다.
     * */
    @Transactional
    public BrandRegisterResult registerBrand(BrandRegisterCommand command) {
        Brand brand = Brand.create(command.name(), command.description(), command.memberId());

        Brand savedModel = brandRepository.register(brand).orElseThrow(() -> {
            return new RuntimeException("브랜드 등록에 실패했습니다.");
        });

        return BrandRegisterResult.of(savedModel);
    }

    /*
     * 브랜드를 수정한다.
     * */
    @Transactional
    public BrandUpdateResult updateBrand(BrandUpdateCommand command) {
        Brand findBrand = brandRepository.findById(command.id()).orElseThrow(() -> {
            return new RuntimeException("브랜드를 찾을 수 없습니다.");
        });

        findBrand.updateBrandInfo(command.name(), command.description(), command.memberId());

        return BrandUpdateResult.of(findBrand);
    }

    /*
     * 브랜드 목록을 조회한다.
     * */
    @Transactional(readOnly = true)
    public List<BrandListResult> getBrandList() {
        return brandRepository.findAll().stream()
                .map(BrandListResult::toResult)
                .toList();
    }

    /*
     * 브랜드 상세 정보를 조회한다.
     * */
    @Transactional(readOnly = true)
    public Brand getBrandDetail(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(() ->
                new CoreException(BrandErrorType.BRAND_NOT_FOUND, "상품의 브랜드를 찾을 수 없습니다. brandId: " + brandId)
        );
    }
}

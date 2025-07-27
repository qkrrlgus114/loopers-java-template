package com.loopers.application.brand;

import com.loopers.application.brand.command.BrandRegisterCommand;
import com.loopers.application.brand.command.BrandUpdateCommand;
import com.loopers.application.brand.result.BrandRegisterResult;
import com.loopers.application.brand.result.BrandUpdateResult;
import com.loopers.domain.brand.BrandModel;
import com.loopers.domain.brand.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        BrandModel brandModel = BrandModel.registerBrand(command.name(), command.description(), command.memberId());

        BrandModel savedModel = brandRepository.register(brandModel).orElseThrow(() -> {
            return new RuntimeException("브랜드 등록에 실패했습니다.");
        });

        return BrandRegisterResult.of(savedModel);
    }

    /*
     * 브랜드를 수정한다.
     * */
    @Transactional
    public BrandUpdateResult updateBrand(BrandUpdateCommand command) {
        BrandModel findBrand = brandRepository.findById(command.id()).orElseThrow(() -> {
            return new RuntimeException("브랜드를 찾을 수 없습니다.");
        });

        findBrand.updateBrandInfo(command.name(), command.description(), command.memberId());

        return BrandUpdateResult.of(findBrand);
    }

}

package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.command.BrandRegisterCommand;
import com.loopers.application.brand.result.BrandRegisterResult;
import com.loopers.application.brand.service.BrandService;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.brand.dto.BrandDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BrandV1Controller {

    private final BrandService brandService;

    /*
     * 브랜드 등록 API
     * */
    @PostMapping("/brands")
    public ApiResponse<BrandDTO.BrandRegisterResponse> registerBrand(
            @RequestBody @Valid BrandDTO.BrandRegisterRequest reqDTO) {
        BrandRegisterCommand command = BrandRegisterCommand.of(
                reqDTO.name(),
                reqDTO.description(),
                reqDTO.memberId()
        );

        BrandRegisterResult result = brandService.registerBrand(command);

        BrandDTO.BrandRegisterResponse resDTO = BrandDTO.BrandRegisterResponse.from(result);

        return ApiResponse.success(resDTO);
    }
}

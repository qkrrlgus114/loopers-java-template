package com.loopers.infrastructure.productlike;

import com.loopers.application.productlike.facade.ProductLikeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLikeSchedule {

    private final ProductLikeFacade productLikeFacade;

    // 1분마다 실행
    @Scheduled(fixedRate = 60 * 1000)
    public void updateProductLikeCount() {
        productLikeFacade.updateAllProductLikeCount();
    }
}

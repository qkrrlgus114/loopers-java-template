package com.loopers.application.stock.service;

import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockRepository;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Stock findStockByProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효하지 않은 상품 ID입니다.");
        }

        return stockRepository.findByProductId(productId);
    }
}

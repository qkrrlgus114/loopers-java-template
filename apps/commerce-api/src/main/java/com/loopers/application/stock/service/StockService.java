package com.loopers.application.stock.service;

import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Stock findStockByProductId(Long productId) {
        return stockRepository.findByProductIdWithLock(productId);
    }

    public void registerStock(Long productId, int stockQuantity) {
        Stock stock = Stock.create(productId, stockQuantity);
        stockRepository.register(stock);
    }
}

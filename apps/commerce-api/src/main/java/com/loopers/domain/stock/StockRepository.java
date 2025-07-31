package com.loopers.domain.stock;

public interface StockRepository {
    Stock findByProductId(Long productId);

    Stock register(Stock stock);

    Stock findById(Long id);
}

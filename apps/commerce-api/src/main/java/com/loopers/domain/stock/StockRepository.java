package com.loopers.domain.stock;

public interface StockRepository {
    Stock findByProductId(Long productId);
}

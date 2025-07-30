package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.Stock;
import com.loopers.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;


    @Override
    public Stock findByProductId(Long productId) {
        return stockJpaRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고를 찾을 수 없습니다. productId: " + productId));
    }
}

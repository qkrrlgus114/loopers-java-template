package com.loopers.application.product.command;

public record ProductListCommand
        (
                Long memberId,
                Integer page, // 페이지 번호
                Integer size, // 크기
                String sortBy,
                String sortDirection
        ) {
}

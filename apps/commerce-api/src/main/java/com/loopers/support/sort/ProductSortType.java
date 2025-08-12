package com.loopers.support.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * 검색 결과 정렬 방향
 * */
@Getter
@RequiredArgsConstructor
public enum ProductSortType {
    LATEST("최신순"),
    PRICE_ASC("가격 오름차순"),
    PRICE_DESC("가격 내림차순"),
    NAME_ASC("이름 오름차순"),
    NAME_DESC("이름 내림차순"),
    LIKE_COUNT_DESC("좋아요 수 내림차순"),
    LIKE_COUNT_ASC("좋아요 수 오름차순");

    private final String description;
}

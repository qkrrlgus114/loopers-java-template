package com.loopers.application.product.result;

import java.util.List;

public record ProductListPage(
        List<ProductListResult> items
) {
    public static ProductListPage of(List<ProductListResult> items) {
        return new ProductListPage(items);
    }
}

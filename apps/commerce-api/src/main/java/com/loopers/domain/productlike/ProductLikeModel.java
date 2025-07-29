package com.loopers.domain.productlike;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_like")
public class ProductLikeModel extends BaseEntity {

    private Long productId;

    private Long memberId;

    protected ProductLikeModel() {
    }

    public ProductLikeModel(Long productId, Long memberId) {
        this.productId = productId;
        this.memberId = memberId;
    }

    public static ProductLikeModel create(Long productId, Long memberId) {
        if (productId == null || productId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 상품 ID가 필요합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        return new ProductLikeModel(productId, memberId);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

}

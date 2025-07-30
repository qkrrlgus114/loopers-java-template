package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String description;

    @Column(nullable = false)
    private Long brandId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private ProductStatus status;

    protected Product() {
    }

    public Product(String name, String description, Long brandId, Long memberId, BigDecimal price, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.brandId = brandId;
        this.memberId = memberId;
        this.price = price;
        this.status = status;
    }

    public static Product create(String name, String description, Long brandId, Long memberId, BigDecimal price) {
        validated(name, description, brandId, memberId, price);

        return new Product(name, description, brandId, memberId, price, ProductStatus.REGISTERED);
    }

    private static void validated(String name, String description, Long brandId, Long memberId, BigDecimal price) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "상품 이름은 1자 이상 20자 이내여야 합니다.");
        }
        if (description == null || description.isBlank() || description.length() > 200 || description.length() < 20) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "상품 설명은 20자 이상 200자 이내여야 합니다.");
        }
        if (brandId == null || brandId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 브랜드 ID가 필요합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "유효한 회원 ID가 필요합니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "상품 가격은 0보다 큰 값이어야 합니다.");
        }
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }
}

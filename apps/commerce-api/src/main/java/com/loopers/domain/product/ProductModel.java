package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class ProductModel extends BaseEntity {

    private String name;

    private String description;

    private Long brandId;
    
    private Long memberId;

    protected ProductModel() {
    }

    public ProductModel(String name, String description, Long brandId, Long memberId) {
        this.name = name;
        this.description = description;
        this.brandId = brandId;
        this.memberId = memberId;
    }

    public static ProductModel create(String name, String description, Long brandId, Long memberId) {
        validated(name, description, brandId, memberId);

        return new ProductModel(name, description, brandId, memberId);
    }

    private static void validated(String name, String description, Long brandId, Long memberId) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("상품 이름은 1자 이상 20자 이내여야 합니다.");
        }
        if (description == null || description.isBlank() || description.length() > 200 || description.length() < 20) {
            throw new IllegalArgumentException("상품 설명은 20자 이상 200자 이내여야 합니다.");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("유효한 브랜드 ID가 필요합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 회원 ID가 필요합니다.");
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

}

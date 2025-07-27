package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand")
public class BrandModel extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;


    private Long memberId;

    private BrandModel(String name, String description, Long memberId) {
        this.name = name;
        this.description = description;
        this.memberId = memberId;
    }

    public static BrandModel registerBrand(String name, String description, Long memberId) {
        validate(name, description, memberId);

        return new BrandModel(name, description, memberId);
    }

    private static void validate(String name, String description, Long memberId) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("브랜드 이름은 1자 이상 20자 이하로 입력해야 합니다.");
        }
        if (description == null || description.isBlank() || description.length() > 255 || description.length() < 10) {
            throw new IllegalArgumentException("브랜드 설명은 10자 이상 255자 이하로 입력해야 합니다.");
        }
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("브랜드 소유자의 ID는 유효한 값이어야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void updateBrandInfo(String name, String description, Long memberId) {
        validate(name, description, memberId);

        this.name = name;
        this.description = description;
    }
}

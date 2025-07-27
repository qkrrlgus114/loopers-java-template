package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.Image;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandModel extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Embedded
    private Image image;

    private BrandModel(String name, String description, Image image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public static BrandModel registerBrand(String name, String description, Image image) {
        validate(name, description, image);

        return new BrandModel(name, description, image);
    }

    private static void validate(String name, String description, Image image) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("브랜드 이름은 1자 이상 20자 이하로 입력해야 합니다.");
        }
        if (description == null || description.isBlank() || description.length() > 255 || description.length() < 10) {
            throw new IllegalArgumentException("브랜드 설명은 1자 이상 255자 이하로 입력해야 합니다.");
        }
        if (image == null) {
            throw new IllegalArgumentException("브랜드 이미지는 필수 입력 사항입니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }
}

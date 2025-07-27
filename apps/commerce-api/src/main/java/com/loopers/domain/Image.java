package com.loopers.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;

@Embeddable
public record Image(

        @NotEmpty(message = "이미지 URL은 필수 입력 사항입니다.")
        @Column(nullable = false, length = 255)
        String url,

        @NotEmpty(message = "썸네일 URL은 필수 입력 사항입니다.")
        @Column(nullable = false, length = 255)
        String thumbnailUrl
) {

    public Image {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수 입력 사항입니다.");
        }
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("썸네일 URL은 필수 입력 사항입니다.");
        }
    }

}

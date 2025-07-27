package com.loopers.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImageTest {

    @DisplayName("이미지 객체를 생성할 때, ")
    @Nested
    class create {

        @DisplayName("이미지 URL과 썸네일 URL이 유효하면 성공한다.")
        @Test
        void successCreate_whenValidUrls() {
            String imageUrl = "http://example.com/image.jpg";
            String thumbnailUrl = "http://example.com/thumbnail.jpg";

            Image image = new Image(imageUrl, thumbnailUrl);

            assertEquals(imageUrl, image.url());
            assertEquals(thumbnailUrl, image.thumbnailUrl());
        }

        @DisplayName("이미지 URL이 null or 빈값이면 예외가 발생한다")
        @NullAndEmptySource
        @ParameterizedTest
        void failCreate_whenUrlIsInvalid(String url) {
            String thumbnailUrl = "http://example.com/thumbnail.jpg";

            assertThrows(IllegalArgumentException.class,
                    () -> new Image(url, thumbnailUrl));
        }

        @DisplayName("썸네일 URL이 null or 빈값이면 예외가 발생한다")
        @NullAndEmptySource
        @ParameterizedTest
        void failCreate_whenThumbnailUrlIsNull(String thumbnailUrl) {
            String imageUrl = "http://example.com/image.jpg";

            assertThrows(IllegalArgumentException.class,
                    () -> new Image(imageUrl, thumbnailUrl));
        }
    }
}

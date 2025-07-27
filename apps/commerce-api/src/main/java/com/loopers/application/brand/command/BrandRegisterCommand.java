package com.loopers.application.brand.command;

/**
 * 브랜드 등록을 위한 커맨드 객체입니다.
 */
public record BrandRegisterCommand(
        String name,
        String description,
        Long memberId
) {
    public static BrandRegisterCommand of(
            String name, String description, Long memberId) {

        return new BrandRegisterCommand(name, description, memberId);
    }
}

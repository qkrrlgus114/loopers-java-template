package com.loopers.support.util;

import org.springframework.stereotype.Component;

/*
 * UUID를 생성하기 위한 유틸 클래스
 * */
@Component
public class UUIDUtil {

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String generateShortUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}

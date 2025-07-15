package com.loopers.interfaces.interceptor;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.MemberErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserIdHeaderInterceptor implements HandlerInterceptor {

    /*
     * 유저 ID 헤더를 검증하는 인터셉터
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String header = request.getHeader("X-USER-ID");

        if (header == null || header.trim().isEmpty()) {
            throw new CoreException(MemberErrorType.MISSING_USER_ID_HEADER, "잘못된 요청입니다.");
        }

        return true;
    }
}

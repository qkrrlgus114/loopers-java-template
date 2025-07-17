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
     *
     * 이 인터셉터는 HTTP 요청 헤더에 "X-USER-ID"가 포함되어 있는지 확인합니다.
     *
     * 만약 헤더가 없거나 비어있다면, CoreException을 발생시켜 잘못된 요청임을 알립니다.
     *
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

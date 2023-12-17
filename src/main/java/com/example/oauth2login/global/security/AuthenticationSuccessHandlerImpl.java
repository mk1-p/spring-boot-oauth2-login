package com.example.oauth2login.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

//        // 서비스 토큰 생성
//        String generateAccessToken = jwtTokenUtil.generateAccessToken(authentication);
//        String generateRefreshToken = jwtTokenUtil.generateRefreshToken(authentication);
//
//        log.info("ACCESS_TOKEN : {}",generateAccessToken);
//        log.info("REFRESH_TOKEN : {}",generateRefreshToken);
//
//        // 토큰 쿠키 설정
//        Cookie accessCookie = new Cookie("access", generateAccessToken);
//        accessCookie.setMaxAge(60);
//        accessCookie.setPath("/token");
//
//        Cookie refreshCookie = new Cookie("refresh", generateRefreshToken);
//        refreshCookie.setMaxAge(60);
//        refreshCookie.setPath("/token");
//
//        // Response에 쿠키 추가
//        response.addCookie(accessCookie);
//        response.addCookie(refreshCookie);

        response.sendRedirect("/");

    }
}

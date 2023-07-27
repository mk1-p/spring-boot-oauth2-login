package com.example.oauth2login.handler;


import com.example.oauth2login.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Get the access token and refresh token from the authorized client
        log.info(">>> onAuthenticationSuccess");

        // 토큰 발행
        String token = jwtTokenUtil.generateToken(authentication);
        // 토큰 헤더에 포함시켜 전송
        // 헤더 말고 바디에 포함 시켜도 무관!
        response.addHeader("Authorization","Bear "+token);

        // Redirect URL 세팅
        response.sendRedirect("/login/login-success");

        super.onAuthenticationSuccess(request, response, authentication);
    }


}

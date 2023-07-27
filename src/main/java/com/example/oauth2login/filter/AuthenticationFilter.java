package com.example.oauth2login.filter;

import com.example.oauth2login.member.Member;
import com.example.oauth2login.member.MemberRepository;
import com.example.oauth2login.model.Role;
import com.example.oauth2login.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends GenericFilterBean {

    // Token Util
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    private final MemberRepository memberRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Request Header 에 Token 또는 API Key 를 추출
        String authorizationHeader = httpRequest.getHeader("Authorization");
        String apiKeyHeader = httpRequest.getHeader("X-API-KEY");

        UsernamePasswordAuthenticationToken authentication;

        // API Key 또는 Bearer Token 둘 중 하나로 인증 하기 위한 작업
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 부분 추출

            if (jwtTokenUtil.validateToken(bearerToken)) {
                // 유효기간에 대한 예외처리
                // ...
            }

            // 토큰에서 Member 조회를 위한 값 추출
            String attributeId = jwtTokenUtil.getUserIdFromToken(bearerToken);
            String registrationId = jwtTokenUtil.getRegistrationIdFromToken(bearerToken);
            // 등록된 Member 조회
            Member member = memberRepository.findByAttributeIdAndRegistrationId(attributeId, registrationId)
                    .orElseThrow(() -> new RuntimeException("Bearer Token 인증 실패!!"));

            // 유저 인가 정보 설정 -> API 별 권한 체크에 사용됨
            authentication = new UsernamePasswordAuthenticationToken(bearerToken, null,
                    // 조회된 Member 에서 권한(Role) 입력
                    Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())));

        } else if (apiKeyHeader != null) {
            String apiKey = apiKeyHeader;
            // API Key를 이용하여 처리 (예: 인증 처리)
            // ...
            authentication = new UsernamePasswordAuthenticationToken(apiKey, null,
                    Collections.singleton(new SimpleGrantedAuthority(Role.API.getKey())));

        } else {
            // 예외처리
            // Guest 권한으로 처리(혹은 권한 인증 실패에 대한 예외처리를 할 것)
            authentication = new UsernamePasswordAuthenticationToken(UUID.randomUUID(), null,
                    Collections.singleton(new SimpleGrantedAuthority(Role.GUEST.getKey())));
        }


        // authentication 세팅
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}

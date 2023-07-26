package com.example.oauth2login.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    // JWT 비밀키 (임의로 설정)
    private String secret = "your_secret_key_here";

    // JWT 유효 시간 설정 (30분으로 설정)
    private long expiration = 1800000;

    // JWT 토큰 생성
    public String generateToken(Authentication authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String clientRegistrationId = getClientRegistrationId(authentication);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("registration_id",clientRegistrationId)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // JWT 토큰으로부터 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public String getRegistrationId(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return (String) claims.getBody().get("registration_id");
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    private String getClientRegistrationId(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        return oauthToken.getAuthorizedClientRegistrationId();
    }

}

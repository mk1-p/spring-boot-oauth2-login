package com.example.oauth2login.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    // JWT 비밀키 (임의로 설정)
    // JWT 키는 되도록이면 길고 쉽게 풀지 못하는 것으로!
    private String REQ_SECRET = "aschnhkghgrrHRoiwoASqfo123kl1l23jlwfmnan19047ahnfgklalkwwikdrkACACjsjUIUKJBlhWAFWASFascWAfaollas";
    // 회원가입 시, 간편로그인 인증 후 추가 정보에 접속하기 위한 토큰 - 1회성 토큰
    private String REGISTRY_SECRET = "";

    // JWT 유효 시간 설정 (30분으로 설정)
    private long ACC_EXPIR = 1800000;
    private long REF_EXPIR = 1000 * 60 * 60 * 24 * 365;
    private long REG_EXPIR = 1000 * 60 * 30;



    // JWT 토큰 생성
    public String generateAccessToken() {
        Map<String, Object> claims = new HashMap<>();

        return generateJWT(claims,REQ_SECRET,ACC_EXPIR);
    }

    public String generateRefreshToken() {
        Map<String, Object> claims = new HashMap<>();

        return generateJWT(claims,REQ_SECRET,REF_EXPIR);
    }


    /**
     * 회원 가입, 간편 인증 후 추가 정보 입력 란 진행 시에 발급 되는 토큰
     * @param authentication
     * @return
     */
    public String generateRegisterToken(Authentication authentication) {

        // google, kakao ...
        String clientRegistrationId = getRegistrationIdFromAuthentic(authentication);

        Map<String, String> claims = new HashMap<>();
        claims.put("subject",authentication.getName());
        claims.put("registration_id",clientRegistrationId);


        return generateJWT(claims, REGISTRY_SECRET, REG_EXPIR);

    }

    /**
     * JWT 생성 Base Method
     * @param claims
     * @param secret
     * @param expireTime
     * @return
     */

    public String generateJWT(Map<String,?> claims, String secret, long expireTime) {
        // Key 세팅
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // 현재시간, 만료시간 세팅
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key,SignatureAlgorithm.HS512) //or signWith(Key, SignatureAlgorithm)
                .compact();
    }


    // JWT 토큰으로부터 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(REQ_SECRET).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    // JWT 토큰으로부터 registrationId 추출 (google, kakao)
    public String getRegistrationIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(REQ_SECRET).parseClaimsJws(token);
        return (String) claims.getBody().get("registration_id");
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(REQ_SECRET).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // RegistrationId 를 얻기 위한 메서드 (From. Authentic)
    private String getRegistrationIdFromAuthentic(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        return oauthToken.getAuthorizedClientRegistrationId();
    }

}

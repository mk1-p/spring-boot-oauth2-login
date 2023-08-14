package com.example.oauth2login.application;

import com.example.oauth2login.dto.OAuthAttributes;
import com.example.oauth2login.member.Member;
import com.example.oauth2login.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 로그인 서비스 구분 값 google, kakao, naver, apple ...
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // 각기 다른 서비스를 통한 OAuth2 로그인을 통합 객체로 만들어준다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    /**
     * Member 조회 후 저장 및 세팅
     * @param attributes
     * @return
     */
    private Member saveOrUpdate(OAuthAttributes attributes) {

        // Member 객체 조회 및 세팅
        Member member = memberRepository.findByAttributeIdAndRegistrationId(attributes.getAttributeId(), attributes.getRegistrationId())
                .map(entity -> entity.updateNameAndEmail(entity.getName(),entity.getEmail()))
                .orElseGet(() -> Member.toEntity(attributes));

        return memberRepository.save(member);
    }

}

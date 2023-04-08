package todo.list_service.config.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import todo.list_service.config.auth.dto.OAuthAttributes;
import todo.list_service.config.auth.dto.SessionUser;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Random;

//OAuth2UserService를 구현하여 로그인 이후 가져온 사용자 정보들을 기반으로 가입, 정보 수정등 기능 지원
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId(); // 현재 로그인중인 서비스가 무엇인지

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();  // OAuth2 로그인 진행시 키가 되는 필드값(PK), 구글의 기본코드는 sub, 네이버는 이 메서드로 보이지 않아 직접 입력 필요

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user",new SessionUser(user));

        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

        return defaultOAuth2User;
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        if (user.getNickName() == null){
            //a~z로 이루어진 10자리 랜덤문자열 생성
            Random random = new Random();
            String generatedString = random.ints(97, 122 + 1)
                    .limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            user.nickNameSetting(generatedString);
        }
        return userRepository.save(user);
    }
}

package todo.list_service.config.oauth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import todo.list_service.config.oauth.dto.OAuthAttributes;
import todo.list_service.config.oauth.dto.SessionUser;
import todo.list_service.domain.user.User;
import todo.list_service.domain.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Random;


/**
 * 설명 : OAuth2UserService를 구현하여 로그인 이후 가져온 사용자 정보들을 기반으로 가입, 정보 수정등 기능 지원
* */


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /* OAuth2 서비스 구분코드 (구글, 카카오, 네이버) */
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        /* OAuth2 로그인 진행시 키가 되는 필드값(PK), 구글의 기본코드는 sub, 네이버는 이 메서드로 보이지 않아 직접 입력 필요 */
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        /* OAuthAttributes 객체 생성 */
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        /* 저장할 User 객체 생성 혹은 수정 */
        User user = saveOrUpdate(attributes);

        /* 세션 정보를 저장하는 직렬화된 dto 클래스 */
        httpSession.setAttribute("user",new SessionUser(user));


        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

        return defaultOAuth2User;
    }

    /**
     * 설명 : 소셜로그인시 이메일에 매칭되는 기존 회원이 존재하면 이름과 프로필사진, 수정날짜만 업데이트하고 기존의 데이터는 그대로 보존
     * */
    private User saveOrUpdate(OAuthAttributes attributes) {
        // User 불러오기
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        /* 처음 가입한 회원의 경우 10문자로 이루어진 랜덤 닉네임 생성*/

        // 닉네임 없을경우 랜덤 생성
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

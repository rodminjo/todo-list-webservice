package todo.list_service.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import todo.list_service.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()// h2 콘솔창에 보이도록 설정
                .and()
                    .authorizeRequests() // URL별 권한 관리 시작
                    .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll() // 인덱스나 css조회, 이미지 조회 등 url는 전체 열람
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //api/vi ... 글 등록 등은 "USER"(=Role.USER.name())만 권한줌
                    .anyRequest().authenticated()// 나머지 사이트는 허가된(로그인된) 사용자만 허가
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login().userInfoEndpoint()//OAuth2 로그인 성공했을때 설정
                    .userService(customOAuth2UserService); //로그인 성공시 수행할 UserService 구현체 지정

        return http.build();
    }

}

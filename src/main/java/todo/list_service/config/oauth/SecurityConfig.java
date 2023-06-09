package todo.list_service.config.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import todo.list_service.domain.user.Role;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable().headers().frameOptions().disable()// csrf와 x-frame-option 비활성화. h2-console 사용을 위해 잠시 disable
                .and()
                    .authorizeRequests() // URL별 권한 관리 시작
                    .antMatchers("/","/main","/jquery/**","/assets/**","/css/**","/images/**","/js/**","/h2-console/**","/login","/display").permitAll() // 인덱스나 css조회, 이미지 조회 등 url는 전체 열람
                    .antMatchers("/posts","/ranking/**").permitAll()   // posts, ranking 게시판은 모든 사용자가 볼 수 있음
                    .antMatchers("/api/v1/**").hasAnyRole(Role.USER.name(),Role.ADMIN.name()) //api/vi ... 글 등록 등은 "USER"(=Role.USER.name()), ADMIN만 권한줌
                    .anyRequest().authenticated()// 나머지 사이트는 허가된(로그인된) 사용자만 허가
                .and()
                    .logout().logoutSuccessUrl("/")
                .and()
                    .oauth2Login().userInfoEndpoint()//OAuth2 로그인 성공했을때 설정
                    .userService(customOAuth2UserService); //로그인 성공시 수행할 UserService 구현체 지정

        http
            .sessionManagement()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            .expiredUrl("/");

        return http.build();
    }

}

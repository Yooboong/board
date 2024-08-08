package com.yooboong.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경 설정 파일, 스프링 시큐리티를 설정하기 위해 사용
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 함, 스프링 시큐리티 활성화
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 애너테이션이 동작할 수 있도록하는 어노테이션
public class SecurityConfig {

    @Bean // 스프링에 의해 생성 및 관리되는 객체임을 선언(@Configuration과 함께 사용)
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")) // 해당 경로에 대한 인가 설정 지정
                        .permitAll()) //해당 경로에 대한 모든 요청을 인가
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin((formLogin) -> formLogin // 로그인 설정
                        .loginPage("/login") // 로그인 페이지 url, GET, 인증이 필요한 주소 요청시 실행됨
                        .defaultSuccessUrl("/")) // 성공시 이동할 url
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login") // OAuth2 로그인 페이지 설정
                        .defaultSuccessUrl("/"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 url 설정
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 루트페이지("/")로 이동
                        .invalidateHttpSession(true)); // 로그아웃 시 생성된 사용자 세션 삭제
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
        // AuthenticationManager 는 스프링 시큐리티의 인증을 처리
        // AuthenticationManager 는 사용자 인증 시
        // 작성한 UserSecurityService와 PasswordEncoder를 내부적으로 사용하여
        // 인증과 권한 부여 프로세스를 처리
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

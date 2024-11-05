package com.cloudy.global.config;

import com.cloudy.global.config.security.ExceptionHandlerFilter;
import com.cloudy.global.config.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    // ⭐️ CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://i11a607.p.ssafy.io", "http://10.0.2.2:3000","https://mono-repo-practice.vercel.app/"));
            config.setAllowCredentials(true);
            return config;
        };
    }

    // Password 인코딩 방식에 BCrypt 암호화 방식 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가!
                .requestMatchers("/error", "/favicon.ico","/swagger-ui/**","/swagger-resources/**","/v3/api-docs/**",
                        "/swagger-ui.html/**", "/companies");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 기능 비활성화, 세션을 사용하지 않기때문에
                .csrf((csrfConfig) ->
                        csrfConfig.disable()
                )
                // Clickjacking 공격을 방지하기 위한 X-Frame-Options 헤더 비활성화
                // 캐시 제어 비활성화
                .headers((headerConfig) ->
                        headerConfig.frameOptions(frameOptionsConfig ->
                                        frameOptionsConfig.disable()
                                )
                                .cacheControl(cacheControl -> cacheControl.disable())

                )
                // session 사용 X 명시
                .sessionManagement((sessionConfig) ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))

                // HTTP 요청에 대한 보안 설정
                // todo: 보안 끄면 api 정상실행
                // todo: roll 부여의 의문점
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        // '/login', '/oauth2' 나머지 모든 요청은 인증된 사용자만 접근 가능
                                        .requestMatchers("/auth/**"
                                                ,"/error", "login/oauth2/code/kakao","/swagger-ui/**"
                                                ,"/swagger-resources/**", "/v3/api-docs/**", "/companies").permitAll()
//                                        .requestMatchers("/**").permitAll()
//                                        .requestMatchers("/admin").hasRole("ADMIN") // admin은 ADMIN 롤만
                                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}

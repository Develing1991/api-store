package org.delivery.storeadmin.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity // security 활성화
public class SecurityConfig {

    // /swagger-ui/index.html 이렇게 작성하면 안됨
    private List<String> SWAGGER = List.of( "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                            ).permitAll() // resource에 대해서는 모든 요청 허용
                            .mvcMatchers(SWAGGER.toArray(new String[0])).permitAll() // swagger 인증 없이 통과
                            .mvcMatchers("/open-api/**").permitAll() // open-api 하위 모든 주소 인증 없이 통과
                            .anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults()) // 폼 로그인 디폴트
                ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // hash + salt
        return new BCryptPasswordEncoder();
    }
}

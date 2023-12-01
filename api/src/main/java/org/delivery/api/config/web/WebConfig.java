package org.delivery.api.config.web;

import lombok.RequiredArgsConstructor;
import org.delivery.api.interceptor.AuthorizationInterceptor;
import org.delivery.api.resolver.UserSessionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    // 추가
    private final UserSessionResolver userSessionResolver;

    private List<String> OPEN_API = List.of("/open-api/**");
    private List<String> DEFAULT_EXCLUDE = List.of("/", "favicon.ico", "/error");
    private List<String> SWAGGER = List.of("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 모든 요청은 인증을 하게 하는데.. 그중 제외 할것들 추가
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(OPEN_API)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER)
        ;

    }

    // 추가
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionResolver);
    }
}

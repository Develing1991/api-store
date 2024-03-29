package org.delivery.api.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.TokenErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        log.info("Authorization Interceptor: {}", request.getRequestURI());
        
        // WEB - chrome의 경우 요청 하기 전에 OPTION 이라는 API를 요청 해서 해당 메소드를 지원 하는지 체크 하는 API가 있음 (pre flight)
        // 그래서 이것을 통과 시켜 줄것임
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        // js, html, png 같은 리소스를 요청하는 경우 통과
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        // TODO header 검증
        var accessToken = request.getHeader("authorization-token");
        if(accessToken == null){
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }
        // 토큰 validation 실행
        var userId = tokenBusiness.validationAccessToken(accessToken);

        // 토큰 validation 통과 했는데 userId가 없다? 정상적인 토큰 아님
        if(userId != null){
            // requestContext: 한가지 request 객체에 global하게 저장할 수 있는 영역
            // 이 값을 스레드 로컬에 저장 (추후 자바에서 공부 ㄱㄱ)
            var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            // Session에 저장할건지 request 단위로 저장 할건지 scope지정
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST); // <- 이번 요청 동안만 저장
            return true;
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
        // return false; // 인증 실패
    }
}

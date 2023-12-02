package org.delivery.api.domain.user.business;

import lombok.RequiredArgsConstructor;
import org.delivery.common.annotation.Business;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.converter.UserConverter;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.user.service.UserService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;
import java.util.Optional;

@Business
@RequiredArgsConstructor
public class UserBusiness {
    private final UserService userService;
    private final UserConverter userConverter;
    private final TokenBusiness tokenBusiness;

    /**
     * 사용자 가입 처리
     * 1. request -> entity
     * 2. entity -> save
     * 3. saved Entity -> response
     * 4. response return
     */
    public UserResponse register(UserRegisterRequest request) {

        var entity = userConverter.toEntity(request);
        var newEntity = userService.register(entity);
        var response = userConverter.toResponse(newEntity);
        return response;

        // 람다식. 코드의 수준은 동일하지 않기 때문에 모두가 알아볼 수 있는 위의 코드
        // 취향에 맞게
/*        return Optional.ofNullable(request)
                .map(userConverter::toEntity)
                .map(userService::register)
                .map(userConverter::toResponse)
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT, "request Null"));*/
    }


    /**
     * 1. email, password를 가지고 사용자 체크
     * 2. user entity 로그인 확인
     * 3. token 생성
     * 4. token response
     * @param request
     */
//    public UserResponse login(UserLoginRequest request) {
    public TokenResponse login(UserLoginRequest request) {
        // 1. email, password를 가지고 사용자 체크
        var userEntity = userService.login(request.getEmail(), request.getPassword());

        // TODO 2.Token 생성 로직으로 변경 하기
        return tokenBusiness.issueToken(userEntity);
//        return userConverter.toResponse(userEntity);
    }

    public UserResponse me(User user) {
        // 이런 코드를 매번 넣기는 불편함..
        // AOP 기반으로 만들어진 기능이 있다.
//        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
//        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);


        //var userEntity = userService.getRegisteredUserWithThrow(Long.parseLong(userId.toString()));

        // 사실상 이미 리졸버에서 유저를 조회했기 때문에 굳이 조회할 필요 없다
        var userEntity = userService.getRegisteredUserWithThrow(user.getId());

        // user를 userConverter를 통해 UserResponse로 바꿔주는 컨버터 메소드 추가해서 사용하는 방향으로
        return userConverter.toResponse(userEntity);
    }
}

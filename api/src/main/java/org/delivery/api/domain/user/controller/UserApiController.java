package org.delivery.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

// 인증 확인 하는 api
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserBusiness userBusiness;

    @GetMapping("/me")
    public Api<UserResponse> me(@UserSession User user){


        // @UserSession User user 이것이 달리는 코드는 이제 리졸버에서 userEntity를 조회하고 User 객체에 담아주니
        // 사실상 user를 조회하는 코드는 쓸일이 거의 없어진 셈  @UserSession User user이것만 파라미터로 달아주면 되니까

//        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
//        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
//        var response = userBusiness.me(Long.parseLong(userId.toString()));
        var response = userBusiness.me(user);
        return Api.OK(response);
    }
}

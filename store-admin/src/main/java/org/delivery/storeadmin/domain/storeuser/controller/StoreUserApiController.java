package org.delivery.storeadmin.domain.storeuser.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import org.delivery.storeadmin.domain.storeuser.converter.StoreUserConverter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store-user")
@RequiredArgsConstructor
public class StoreUserApiController {

    private final StoreUserConverter storeUserConverter;

    @GetMapping("/me")
    public StoreUserResponse me(
            @Parameter(hidden = true) // 스웨거에서 안보이게
            //우리가 @UserSession만든 것처럼 security에는 @AuthenticationPrincipal가 있음
            @AuthenticationPrincipal UserSession userSession // 객체는 UserDetails를 구현한 객체여야 함
    ){
        return storeUserConverter.toResponse(userSession);
    }
}

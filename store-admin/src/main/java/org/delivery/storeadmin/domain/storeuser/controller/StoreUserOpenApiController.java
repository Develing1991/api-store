package org.delivery.storeadmin.domain.storeuser.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.storeadmin.domain.storeuser.business.StoreUserBusiness;
import org.delivery.storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import org.delivery.storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/open-api/store-user")
@RequiredArgsConstructor
public class StoreUserOpenApiController {

    public final StoreUserBusiness storeUserBusiness;

    // Api Spec, @Business어노 같은 같이 쓸만한 걸 :common 모듈로 사용 ㄱㄱ
    @PostMapping("")
    public StoreUserResponse register(@Valid @RequestBody StoreUserRegisterRequest storeUserRegisterRequest){
        var response = storeUserBusiness.register(storeUserRegisterRequest);
        return response;
    }
}

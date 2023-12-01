package org.delivery.api.domain.userorder.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.business.UserOrderBusiness;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/api-order")
@RequiredArgsConstructor
public class UserOrderApiController {
    private final UserOrderBusiness userOrderBusiness;

    // 주문
    @PostMapping("")
    public Api<UserOrderResponse> userOrder(@Valid @RequestBody Api<UserOrderRequest> userOrderRequest,
                                            @Parameter(hidden = true) // User는 Swagger에서 파라미로 사용안함
                                            @UserSession User user){
        var response = userOrderBusiness.userOrder(user,
                                                   userOrderRequest.getBody());
        return Api.OK(response);
    }


    // 현재 진행 중인 주문건
    @GetMapping("/current")
    public Api<List<UserOrderDetailResponse>> current( @Parameter(hidden = true) @UserSession User user ){
        var resopnse = userOrderBusiness.current(user);
        return Api.OK(resopnse);
    }

    // 과거 주문 내역

    @GetMapping("/history")
    public Api<List<UserOrderDetailResponse>> history( @Parameter(hidden = true) @UserSession User user ){
        var resopnse = userOrderBusiness.history(user);
        return Api.OK(resopnse);
    }

    // 주문 1건에 대한 상세 내역
    @GetMapping("/id/{orderId}")
    public Api<UserOrderDetailResponse> read ( @Parameter(hidden = true) @UserSession User user,
                                               @PathVariable Long orderId){
        var response = userOrderBusiness.read(user, orderId);
        return Api.OK(response);
    }
}

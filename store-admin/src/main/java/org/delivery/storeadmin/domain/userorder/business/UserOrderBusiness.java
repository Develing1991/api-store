package org.delivery.storeadmin.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.common.message.model.UserOrderMessage;
import org.delivery.storeadmin.common.annotation.Business;
import org.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import org.delivery.storeadmin.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.storeadmin.domain.storemenu.service.StoreMenuService;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.storeadmin.domain.userorder.converter.UserOrderConverter;
import org.delivery.storeadmin.domain.userorder.service.UserOrderService;
import org.delivery.storeadmin.domain.userordermenu.service.UserOrderMenuService;

import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {
    private final UserOrderService userOrderService;
    private final UserOrderMenuService userOrderMenuService;
    private final UserOrderConverter userOrderConverter;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;
    private final SseConnectionPool sseConnectionPool;

    /**
     * 주문
     * 주문 내역 찾기
     * 스토어 찾기
     * 연결된 세션 찾아서 push
     */
    public void pushUserOrder(UserOrderMessage userOrderMessage){ // orderId
        // user order entity 주문을 찾기
        var userOrderEntity = userOrderService.
                getUserOrder(userOrderMessage.getUserOrderId()).
                orElseThrow(()->new RuntimeException("사용자 주문 없음"));


        // response는 두가지를 내려줄거임 사용자의 주문(user_order)과 사용자의 주문메뉴리스트(List<store_menu>)

        // response - user_order
        var userOrderResponse = userOrderConverter.toResponse(userOrderEntity);

        // response - List<store_menu>
        // user order menu (중간 테이블) -> 해당 주문 id로 user_order_menu 그냥 다 찾아 와서
        var userOrderMenuList = userOrderMenuService.getUserOderMenuList(userOrderEntity.getId());
        var storeMenuResponseList = userOrderMenuList.stream()
                .map(userOrderMenuEntity ->{
                    // userOrderMenu 있는 storeMenuId들로 다시 storeMenu들 다 찾아와서
                    return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                })
                .map(storeMenuEntity -> {
                    // storeMenu들을 Response로 변경
                    return storeMenuConverter.toResponse(storeMenuEntity);
                })
                .collect(Collectors.toList());


        // push userOrder와 List<storeMenu>를 묶어서 내려줌
        var pushResponse = UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderResponse)
                .storeMenuResponsesList(storeMenuResponseList)
                .build();


        // store_user (가맹점 점주 사용자) 에게 push
        // storeId로 할거면 UserSession의 storeId를 uniqueKey로 사용해야 됨
//        var userConnection = sseConnectionPool.getSession(userOrderEntity.getStoreId().toString());
        var userConnection = sseConnectionPool.getSession(userOrderEntity.getUserId().toString());
        userConnection.sendMessage(pushResponse);
    }
}

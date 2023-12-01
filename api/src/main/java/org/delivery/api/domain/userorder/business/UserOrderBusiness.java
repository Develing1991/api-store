package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.producer.UserOrderProducer;
import org.delivery.api.domain.userorder.service.UserOrderSerivce;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.userorder.UserOrderEntity;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {

    private final UserOrderSerivce userOrderSerivce;
    private final StoreService storeService;
    private final StoreConverter storeConverter;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;
    private final UserOrderProducer userOrderProducer; // ################### 추가 ######################

    // 1. 사용자, 메뉴 아이디 리스트 (한 주문에 상품은 여러개 주문 가능하니까)
    // 2. userOrder 생성 (주문)
    // 3. userOrderMenu 생성 (user_order_id, store_menu_id)
    // 4. 응답
    public UserOrderResponse userOrder(User user, UserOrderRequest userOrderRequest) { // 사용자, [ 1, 2 ] 상품 id 리스트
        // storeMenuIdList로 storeMenu들을 불러옴
        var storeMenuEntityList = userOrderRequest.getStoreMenuIdList()
                                                .stream()
                                                .map(it -> storeMenuService.getStoreMenuWithThrow(it))
                                                .collect(Collectors.toList());

        // 어떤 사용자가 주문 했는지와(userId), storeMenu들의 amount들을 다 더해서 UserOrderEntity의 amount에 셋팅
        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList, userOrderRequest.getStoreId());

        // 2. userOrder [save]
        var newUserOrderEntity = userOrderSerivce.order(userOrderEntity);

        // 3. userOrderMenu (중간 테이블 데이터)생성 (user_order_id, store_menu_id) 주문 목록 생성
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(storeMenuEntity -> {
                    return userOrderMenuConverter.toEntity(newUserOrderEntity.getId(), storeMenuEntity.getId());
                }).collect(Collectors.toList());
        // userOrderMenu [save]
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        // 비동기로 가맹점에 주문 알리기 // ################### 추가 ######################
        userOrderProducer.sendOrder(newUserOrderEntity);

        // response
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    public List<UserOrderDetailResponse> current(User user) {
        var userOrderEntityList = userOrderSerivce.current(user.getId());
        // 주문 1건씩 처리
        var userOrderDetailResponseList =
                userOrderEntityList.stream()
                        .map(it -> {
                            // 사용자가 주문한 메뉴
                            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenuId());
                                return storeMenuEntity;
                            }).collect(Collectors.toList());

                            // 사용자가 주문한 스토어 Todo 리팩토링 필요
                            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                                                          .stream().findFirst().get().getStoreId());

                            return  UserOrderDetailResponse.builder()
                                    .userOrderResponse(userOrderConverter.toResponse(it))
                                    .storeMenuResponseList(
                                            storeMenuEntityList
                                                    .stream()
                                                    .map(storeMenuConverter::toResponse)
                                                    .collect(Collectors.toList())
                                    )
                                    .storeResponse(storeConverter.toResponse(storeEntity))
                                    .build();
                        }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {
        var userOrderEntityList = userOrderSerivce.history(user.getId());
        // 주문 1건씩 처리
        var userOrderDetailResponseList =
                userOrderEntityList.stream()
                        .map(it -> {
                            // 사용자가 주문한 메뉴
                            var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenuId());
                                return storeMenuEntity;
                            }).collect(Collectors.toList());

                            // 사용자가 주문한 스토어 Todo 리팩토링 필요
                            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                                    .stream().findFirst().get().getStoreId());

                            return  UserOrderDetailResponse.builder()
                                    .userOrderResponse(userOrderConverter.toResponse(it))
                                    .storeMenuResponseList(
                                            storeMenuEntityList
                                                    .stream()
                                                    .map(storeMenuConverter::toResponse)
                                                    .collect(Collectors.toList())
                                    )
                                    .storeResponse(storeConverter.toResponse(storeEntity))
                                    .build();
                        }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {
        var userOrderEntity = userOrderSerivce.getUserOrderWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
        var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
            var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenuId());
            return storeMenuEntity;
        }).collect(Collectors.toList());

        // 사용자가 주문한 스토어 Todo 리팩토링 필요
        var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                .stream().findFirst().get().getStoreId());

        return  UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponseList(
                        storeMenuEntityList
                                .stream()
                                .map(storeMenuConverter::toResponse)
                                .collect(Collectors.toList())
                )
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build();
    }

}

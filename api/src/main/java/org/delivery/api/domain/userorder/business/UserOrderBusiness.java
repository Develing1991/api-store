package org.delivery.api.domain.userorder.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.common.annotation.Business;
import org.delivery.common.api.Api;
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
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    private final ObjectMapper objectMapper;

    // 1. 사용자, 메뉴 아이디 리스트 (한 주문에 상품은 여러개 주문 가능하니까)
    // 2. userOrder 생성 (주문)
    // 3. userOrderMenu 생성 (user_order_id, store_menu_id)
    // 4. 응답
    public UserOrderResponse userOrder(User user, UserOrderRequest userOrderRequest) { // 사용자, [ 1, 2 ] 상품 id 리스트
        var storeEntity = storeService.getStoreWithThrow(userOrderRequest.getStoreId());

        // storeMenuIdList로 storeMenu들을 불러옴
        var storeMenuEntityList = userOrderRequest.getStoreMenuIdList()
                                                .stream()
                                                .map(it -> storeMenuService.getStoreMenuWithThrow(it))
                                                .collect(Collectors.toList());

        // 어떤 사용자가 주문 했는지와(userId), storeMenu들의 amount들을 다 더해서 UserOrderEntity의 amount에 셋팅
        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList,
                                                                        //userOrderRequest.getStoreId()
                                                                        storeEntity);

        // 2. userOrder [save]
        var newUserOrderEntity = userOrderSerivce.order(userOrderEntity);

        // 3. userOrderMenu (중간 테이블 데이터)생성 (user_order_id, store_menu_id) 주문 목록 생성
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(storeMenuEntity -> {
                    return userOrderMenuConverter.toEntity(newUserOrderEntity, storeMenuEntity);
                }).collect(Collectors.toList());
        // userOrderMenu [save]
        userOrderMenuEntityList.forEach(userOrderMenuEntity -> userOrderMenuService.order(userOrderMenuEntity));

        // 비동기로 가맹점에 주문 알리기 // ################### 추가 ######################
        userOrderProducer.sendOrder(newUserOrderEntity);

        // response
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    public List<UserOrderDetailResponse> current(@NotNull User user) {
        var userOrderEntityList = userOrderSerivce.current(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList =
                userOrderEntityList.stream()
                        .map(userOrderEntity -> {
                            /*
                            // JPA ENTITY 연관 관계 설정 시 서로를 참조하다 보니 생기는 순환참조 테스트
                            log.info("사용자의 주문 정보: {}", userOrderEntity);

                            try {
                                var jsonString = objectMapper.writeValueAsString(it);
                                log.info("json String: {}", jsonString);
                            } catch (JsonProcessingException e) {
                                log.error("", e);
                                throw new RuntimeException(e);
                            }
                            */

                            /* 기존엔 연관관계 맵핑이 없어 일일히 다 쿼리를 날려 줬다
                             jpa로 연관관계를 설정해 두면 알아서 해당 연관 객체의 쿼리를 만들어주니 객체에서 찾으면 됨
                             대신 내가 생각 하고 있는 쿼리가 맞는 지 잘 확인 해야함.
                             또한 양방향 맵핑 일때와 단방향 맵핑일 때를 잘 구분 해서 리소스를 줄이자..*/

                            // 사용자가 주문한 메뉴
                            //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
                            var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList()
                                    .stream()
                                    .filter(userOrderMenuEntity -> userOrderMenuEntity.getStatus().equals(UserOrderMenuStatus.UNREGISTERED))
                                    .collect(Collectors.toList());

                            /*
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenu().getId());
                                return storeMenuEntity;
                            }).collect(Collectors.toList());
                            */
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> uom.getStoreMenu())
                                    .collect(Collectors.toList());

                            // 사용자가 주문한 스토어 Todo 리팩토링 필요
                            /*
                            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                                                          .stream().findFirst().get().getStore().getId());
                            */


                            var storeEntity = userOrderEntity.getStore();

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
                        }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {
        var userOrderEntityList = userOrderSerivce.history(user.getId());
        // 주문 1건씩 처리
        var userOrderDetailResponseList =
                userOrderEntityList.stream()
                        .map(userOrderEntity -> {
                            // 사용자가 주문한 메뉴
                            // var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
                            var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList();

                            /*
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenu().getId());
                                return storeMenuEntity;
                            }).collect(Collectors.toList());
                            */
                            var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> uom.getStoreMenu())
                                    .collect(Collectors.toList());

                            // 사용자가 주문한 스토어 Todo 리팩토링 필요
                            /*
                            var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                                    .stream().findFirst().get().getStore().getId());
                            */

                            var storeEntity = userOrderEntity.getStore();

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
                        }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {
        var userOrderEntity = userOrderSerivce.getUserOrderWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        // var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
        var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList()
                .stream()
                .filter(userOrderMenuEntity -> userOrderMenuEntity.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                .collect(Collectors.toList());
        /*
        var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> {
            var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(uom.getStoreMenu().getId());
            return storeMenuEntity;
        }).collect(Collectors.toList());
        */
        var storeMenuEntityList = userOrderMenuEntityList.stream().map(uom -> uom.getStoreMenu())
                .collect(Collectors.toList());

        // 사용자가 주문한 스토어 Todo 리팩토링 필요
        /*
        var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList
                .stream().findFirst().get().getStore().getId());
        */
        var storeEntity = userOrderEntity.getStore();

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

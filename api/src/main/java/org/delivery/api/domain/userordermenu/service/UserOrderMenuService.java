package org.delivery.api.domain.userordermenu.service;

import lombok.RequiredArgsConstructor;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.db.userordermenu.UserOrderMenuEntity;
import org.delivery.db.userordermenu.UserOrderMenuRepository;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserOrderMenuService {

    private final UserOrderMenuRepository userOrderMenuRepository;

    // 특정 주문의 상품 리스트
    public List<UserOrderMenuEntity> getUserOrderMenu(Long userOrderId) {
        return userOrderMenuRepository.findAllByUserOrderIdAndStatus(userOrderId, UserOrderMenuStatus.REGISTERED);
    }


    // 주문시 user_order_menu 테이블에 user_order_id와 store_menu_id 저장
    public UserOrderMenuEntity order(UserOrderMenuEntity userOrderMenuEntity){
        return Optional.ofNullable(userOrderMenuEntity)
                    .map(it -> {
                        it.setStatus(UserOrderMenuStatus.REGISTERED);
                        return userOrderMenuRepository.save(it);
                    })
                    .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT));
    }
}

package org.delivery.api.domain.userordermenu.converter;

import org.delivery.common.annotation.Converter;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

@Converter
public class UserOrderMenuConverter {
//    public UserOrderMenuEntity toEntity(UserOrderEntity userOrderEntity, StoreMenuEntity storeMenuEntity){
    public UserOrderMenuEntity toEntity(
            // Long user_order_id,
            UserOrderEntity userOrderEntity,
            StoreMenuEntity storeMenuEntity){
        return UserOrderMenuEntity.builder()
                //.userOrderId(user_order_id)
                .userOrder(userOrderEntity)
                .storeMenu(storeMenuEntity)
                .build();
    }
}

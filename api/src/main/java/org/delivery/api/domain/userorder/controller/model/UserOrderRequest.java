package org.delivery.api.domain.userorder.controller.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserOrderRequest {

    @NotNull
    private Long storeId;

    // @UserSession으로 로그인 된 사용자를 받을거기 때문에 필요 없음
    // private Long userId;

    // 특정 메뉴의 리스트를 주문 (여러 개 주문할 수 있기 때문에)
    @NotNull
    private List<Long> storeMenuIdList;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<Long> getStoreMenuIdList() {
        return storeMenuIdList;
    }

    public void setStoreMenuIdList(List<Long> storeMenuIdList) {
        this.storeMenuIdList = storeMenuIdList;
    }
}

package org.delivery.db.storemenu.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreMenuStatus {
    REGISTERED("등록"),
    UNREGISTERED("해지"),
    // 대기중
    // 일시정지 등등..
    ;
    private String description;
}

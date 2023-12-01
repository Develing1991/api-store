package org.delivery.db.store.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreStatus {
    // 등록 대기도 있을 수 있다
    REGISTERED("등록"),
    // 해지 대기(신청) 도 있을 수 있다
    UNREGISTERED("해지"),

    ;
    private final String description;
}

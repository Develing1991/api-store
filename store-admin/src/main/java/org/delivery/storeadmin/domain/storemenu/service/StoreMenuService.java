package org.delivery.storeadmin.domain.storemenu.service;

import lombok.RequiredArgsConstructor;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.user.StoreMenuRepository;
import org.delivery.db.user.enums.StoreMenuStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreMenuService {
    private final StoreMenuRepository storeMenuRepository;
    public StoreMenuEntity getStoreMenuWithThrow(Long id){
        return storeMenuRepository
                .findFirstByIdAndStatusOrderByIdDesc(id, StoreMenuStatus.REGISTERED)
                .orElseThrow(()-> new RuntimeException("스토어 메뉴를 찾지 못함"));
    }
}

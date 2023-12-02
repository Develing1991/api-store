package org.delivery.api.domain.storemenu.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.common.annotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class StoreMenuBusiness {

    private final StoreService storeService;
    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    public StoreMenuResponse register(StoreMenuRegisterRequest request){
        var storeEntity = storeService.getStoreWithThrow(request.getStoreId());
        var entity = storeMenuConverter.toEntity(request, storeEntity);
        var newEntity = storeMenuService.register(entity);
        return storeMenuConverter.toResponse(newEntity);
    }

    public List<StoreMenuResponse> search(Long storeId){
        var list = storeMenuService.getStoreMenuByStoreId(storeId);
        return list.stream()
                            .map(storeMenuConverter::toResponse)
                            .collect(Collectors.toList());
    }
}

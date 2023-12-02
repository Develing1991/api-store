package org.delivery.db.store;

import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    // 특정 유효한 스토어
    // select * from store where id = ? and status = 'REGISTERED' order by id desc limit 1;
    Optional<StoreEntity> findFirstByIdAndStatusOrderByIdDesc(Long id, StoreStatus storeStatus);

    // 유효한 스토어 리스트
    // select * from store where status = 'REGISTERED' order by id desc limit 1;
    List<StoreEntity> findAllByStatusOrderByIdDesc(StoreStatus storeStatus);

    // 유효한 특정 카테고리의 스토어 리스트
    List<StoreEntity> findAllByStatusAndCategoryOrderByStarDesc(StoreStatus storeStatus, StoreCategory storeCategory);

    // select * from store where name = ? and status = ? order by id desc limit 1;
    Optional<StoreEntity> findFirstByNameAndStatusOrderByIdDesc(String name, StoreStatus storeStatus);
}
*/

package org.delivery.db.store

import org.delivery.db.store.enums.StoreCategory
import org.delivery.db.store.enums.StoreStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StoreRepository : JpaRepository<StoreEntity, Long> {
    // 특정 유효한 스토어
    // select * from store where id = ? and status = 'REGISTERED' order by id desc limit 1;
    fun findFirstByIdAndStatusOrderByIdDesc(id: Long?, storeStatus: StoreStatus?): StoreEntity?

    // 유효한 스토어 리스트
    // select * from store where status = 'REGISTERED' order by id desc limit 1;
    fun findAllByStatusOrderByIdDesc(storeStatus: StoreStatus?): List<StoreEntity>

    // 유효한 특정 카테고리의 스토어 리스트
    fun findAllByStatusAndCategoryOrderByStarDesc(
        storeStatus: StoreStatus?,
        storeCategory: StoreCategory?
    ): List<StoreEntity>

    // select * from store where name = ? and status = ? order by id desc limit 1;
    fun findFirstByNameAndStatusOrderByIdDesc(name: String?, storeStatus: StoreStatus?): StoreEntity?
}

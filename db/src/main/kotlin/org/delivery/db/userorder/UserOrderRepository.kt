package org.delivery.db.userorder

import org.delivery.db.userorder.enums.UserOrderStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserOrderRepository : JpaRepository<UserOrderEntity, Long> {
    // 특정 유저의 특정 주문
    // select * from user_order where id = ? and status = ? and user_id = ?;
    //Optional<UserOrderEntity> findAllByIdAndStatusAndUserId(Long id, UserOrderStatus status, Long userId);
    fun findByIdAndStatusAndUserId(id: Long?, status: UserOrderStatus?, userId: Long?): UserOrderEntity?

    // 특정 유저의 모든 주문
    // select * from user_order where user_id = ? and status = ? order by id desc;
    fun findAllByUserIdAndStatusOrderByIdDesc(userId: Long?, status: UserOrderStatus?): List<UserOrderEntity>

    // 특정 유저의 현재 진행 중인 주문 내역 -> ORDER, ACCEPT, COOKING, DELIVERY
    // 과거 주문한 내역 -> RECEIVE
    // select * from user_order where user_id = ? and status in (?, ?... ) order by id desc;
    fun findAllByUserIdAndStatusInOrderByIdDesc(userId: Long?, status: List<UserOrderStatus>?): List<UserOrderEntity>
}

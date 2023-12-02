package org.delivery.api.domain.userorder.business

import org.delivery.api.common.Log
import org.delivery.api.domain.store.converter.StoreConverter
import org.delivery.api.domain.store.service.StoreService
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter
import org.delivery.api.domain.storemenu.service.StoreMenuService
import org.delivery.api.domain.user.model.User
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse
import org.delivery.api.domain.userorder.converter.UserOrderConverter
import org.delivery.api.domain.userorder.producer.UserOrderProducer
import org.delivery.api.domain.userorder.service.UserOrderSerivce
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService
import org.delivery.common.annotation.Business
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus
import kotlin.streams.toList

@Business
class UserOrderBusiness(
    private val userOrderSerivce: UserOrderSerivce,
    private val userOrderConverter: UserOrderConverter,

    private val storeMenuService: StoreMenuService,
    private val storeMenuConverter: StoreMenuConverter,

    private val storeSerivce: StoreService,
    private val storeConverter: StoreConverter,

    private val userOrderMenuService: UserOrderMenuService,
    private val userOrderMenuConverter: UserOrderMenuConverter,

    private val userOrderProducer: UserOrderProducer,
) {
    companion object: Log

    fun log() {
        log.info("ddd")
    }

    fun userOrder(
        user: User?,
        userOrderRequest: UserOrderRequest?
    ): UserOrderResponse{
        val storeEntity = storeSerivce.getStoreWithThrow(userOrderRequest?.storeId)

        val storeMenuEntityList = userOrderRequest?.storeMenuIdList
            ?.mapNotNull { storeMenuService.getStoreMenuWithThrow(it) }
            ?.toList()

        val userOrderEntity = userOrderConverter.toEntity(
            user = user, storeMenuEntityList = storeMenuEntityList, storeEntity = storeEntity)
            .run {
                userOrderSerivce.order(this)
            }

        val userOrderMenuEntityList = storeMenuEntityList
            ?.map { userOrderMenuConverter.toEntity(userOrderEntity, it) }
            ?.toList()

        userOrderMenuEntityList?.forEach { userOrderMenuService.order(it) }

        userOrderProducer.sendOrder(userOrderEntity)

        return userOrderConverter.toResponse(userOrderEntity)
    }

    fun current(
        user: User?
    ): List<UserOrderDetailResponse>? {
        return userOrderSerivce.current(user?.id).map { userOrderEntity ->
            val storeMenuEntityList =
                userOrderEntity.userOrderMenuList?.stream()
                    ?.filter { userOrderMenuEntity -> userOrderMenuEntity.status == UserOrderMenuStatus.REGISTERED }
                    ?.map { userOrderMenuEntity -> userOrderMenuEntity.storeMenu }
                    ?.toList()


                UserOrderDetailResponse(
                    userOrderResponse = userOrderConverter.toResponse(userOrderEntity),
                    storeResponse = storeConverter.toResponse(userOrderEntity.store),
                    storeMenuResponseList = storeMenuEntityList
                        ?.map { storeMenuConverter.toResponse(it) }
                        ?.toList()
                )
            }.toList()
    }
    fun history(
        user: User?
    ): List<UserOrderDetailResponse>? {
        return userOrderSerivce.history(user?.id).map {
                userOrderEntity ->
            val storeMenuEntityList =
                userOrderEntity.userOrderMenuList?.stream()
                    ?.filter { userOrderMenuEntity -> userOrderMenuEntity.status == UserOrderMenuStatus.REGISTERED }
                    ?.map {userOrderMenuEntity -> userOrderMenuEntity.storeMenu }
                    ?.toList()

            UserOrderDetailResponse(
                userOrderResponse = userOrderConverter.toResponse(userOrderEntity),
                storeResponse = storeConverter.toResponse(userOrderEntity.store),
                storeMenuResponseList = storeMenuEntityList
                    ?.map { storeMenuConverter.toResponse(it) }
                    ?.toList()
            )
        }.toList()
    }

    fun read(
        user: User?,
        orderId: Long?,
    ):UserOrderDetailResponse {
        return userOrderSerivce.getUserOrderWithThrow(orderId, user?.id)
            .let { userOrderEntity ->
                val storeMenuEntityList = userOrderEntity.userOrderMenuList
                    ?.stream()
                    ?.filter { it.status == UserOrderMenuStatus.REGISTERED }
                    ?.map { it.storeMenu }
                    ?.toList()
                    ?: listOf()

                return  UserOrderDetailResponse(
                    userOrderResponse = userOrderConverter.toResponse(userOrderEntity),
                    storeResponse = storeConverter.toResponse(userOrderEntity.store),
                    storeMenuResponseList = storeMenuEntityList
                        ?.map { storeMenuConverter.toResponse(it) }
                        ?.toList()
                )
            }

    }
}
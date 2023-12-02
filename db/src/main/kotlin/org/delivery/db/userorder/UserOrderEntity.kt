package org.delivery.db.userorder

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.ToString
import org.delivery.db.store.StoreEntity
import org.delivery.db.userorder.enums.UserOrderStatus
import org.delivery.db.userordermenu.UserOrderMenuEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_order")
//data class UserOrderEntity ( // data class => toString, hash, equals... jpa랑 궁합이 안맞음
class UserOrderEntity ( // data class => toString, hash, equals... jpa랑 궁합이 안맞음
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null, // private 설정 시 getter, setter가 제공되지 않으므로 var

    @field:Column(nullable = false)
    var userId:Long?=null,

    @field:JoinColumn(nullable = false)
    @field:ManyToOne
    var store: StoreEntity?=null,

    @field:Column(length = 50, nullable = false)
    @field:Enumerated(EnumType.STRING)
    var status: UserOrderStatus?=null,

    @field:Column(precision = 11, scale = 4, nullable = false)
    var amount: BigDecimal?=null,
    var orderedAt: LocalDateTime?=null,
    var acceptedAt: LocalDateTime?=null,
    var cookingStartedAt: LocalDateTime?=null,
    var deliveryStartedAt: LocalDateTime?=null,
    var receivedAt: LocalDateTime?=null,

    @field:OneToMany(mappedBy = "userOrder")
    // @field:ToString.Exclude 롬복이라 동작하지 않음
    @field:JsonIgnore // jakson라이브러리라 동작함
    var userOrderMenuList: MutableList<UserOrderMenuEntity>?=null
) {

//     toString이 필요하다면 직접 정의해서 사용하는 것을 추천
//     순환 참조 일어나니 연관 관계 맵핑된 필드는 직접 빼고..
    override fun toString(): String {
        return "UserOrderEntity(id=$id, userId=$userId, store=$store, status=$status, amount=$amount, orderedAt=$orderedAt, acceptedAt=$acceptedAt, cookingStartedAt=$cookingStartedAt, deliveryStartedAt=$deliveryStartedAt, receivedAt=$receivedAt)"
    }
}
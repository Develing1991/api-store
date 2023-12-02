package org.delivery.db.userordermenu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_order_menu")
public class UserOrderMenuEntity extends BaseEntity {
    // @Column(nullable = false)
    // @JoinColumn(nullable = false, name = "user_order_id")
    @JoinColumn(nullable = false)
    @ManyToOne
    private UserOrderEntity userOrder; // user_order_id
    // private Long userOrderId;

    // @Column(nullable = false)
    // @JoinColumn(nullable = false, name = "store_menu_id")
    @ManyToOne
    private StoreMenuEntity storeMenu; // store_menu_id
    // private Long storeMenuId;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOrderMenuStatus status;


}

package com.storage.order;

import com.storage.BaseEntity;
import com.storage.enums.OrderStatus;
import com.storage.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private OrderStatus status;

    @Builder
    public Order(final User user, final OrderStatus status) {
        this.user = user;
        this.status = status;
    }

}


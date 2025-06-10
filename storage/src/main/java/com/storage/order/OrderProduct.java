package com.storage.order;

import com.storage.BaseEntity;
import com.storage.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    @Builder
    public OrderProduct(
        final Order order,
        final Product product,
        final Integer quantity
    ) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

}

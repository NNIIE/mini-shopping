package com.relation.product;

import com.relation.BaseEntity;
import com.relation.brand.Brand;
import com.relation.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Setter
    private String name;

    @Setter
    private BigDecimal price;

    @Setter
    private Integer stock;

    @Builder
    public Product(
        final Brand brand,
        final Category category,
        final String name,
        final BigDecimal price,
        final Integer stock
    ) {
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

}

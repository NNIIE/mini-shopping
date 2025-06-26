package com.relation.productthumbnail;

import com.relation.BaseEntity;
import com.relation.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_thumbnail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductThumbnail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "size_type")
    private String sizeType;

    @Builder
    public ProductThumbnail(
        final Product product,
        final String url,
        final String sizeType
    ) {
        this.product = product;
        this.url = url;
        this.sizeType = sizeType;
    }

}

package com.relation.productimage;

import com.relation.BaseEntity;
import com.relation.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Builder
    public ProductImage(
        final Product product,
        final String url,
        final String fileName,
        final Long fileSize
    ) {
        this.product = product;
        this.url = url;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}

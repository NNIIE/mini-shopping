package com.storage.brand;

import com.storage.BaseEntity;
import com.storage.admin.Admin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Setter
    private String name;
    private String businessNumber;

    @Builder
    public Brand(
        final Admin admin,
        final String name,
        final String businessNumber
    ) {
        this.admin = admin;
        this.name = name;
        this.businessNumber = businessNumber;
    }

}

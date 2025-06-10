package com.storage.account;

import com.storage.BaseEntity;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Builder
    public Account(
        final String email,
        final String password,
        final UserRole role,
        final AccountStatus status
    ) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

}

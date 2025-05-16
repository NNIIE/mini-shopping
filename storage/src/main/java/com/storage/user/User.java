package com.storage.user;

import com.storage.BaseEntity;
import com.storage.account.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String nickname;

    private String phoneNumber;

    @Builder
    public User(
        final Account account,
        final String nickname,
        final String phoneNumber
    ) {
        this.account = account;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

}

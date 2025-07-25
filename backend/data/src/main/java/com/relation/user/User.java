package com.relation.user;

import com.relation.BaseEntity;
import com.relation.account.Account;
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

    @Setter
    private String refreshToken;

    @Builder
    public User(
        final Account account,
        final String nickname,
        final String phoneNumber,
        final String refreshToken
    ) {
        this.account = account;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.refreshToken = refreshToken;
    }

}

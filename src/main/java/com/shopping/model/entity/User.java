package com.shopping.model.entity;

import com.shopping.model.UserRole;
import com.shopping.model.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(length = 128, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private User(
        final String nickname,
        final String email,
        final String password,
        final UserRole role,
        final UserStatus status
    ) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public static User createSignUpUser(
        final String nickname,
        final String email,
        final String encodedPassword,
        final UserRole role
    ) {
        return User.builder()
            .nickname(nickname)
            .email(email)
            .password(encodedPassword)
            .role(role)
            .status(UserStatus.ACTIVE)
            .build();
    }

}

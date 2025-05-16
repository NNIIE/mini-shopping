package com.storage.token;

import com.storage.BaseEntity;
import com.storage.enums.DeviceType;
import com.storage.enums.TokenType;
import com.storage.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private String token;

    @Enumerated(EnumType.STRING)
    private DeviceType device;

    private String ipAddress;

    private Instant issuedAt;

    private Instant expiresAt;

    @Builder
    public Token(
        final Long id,
        final User user,
        final TokenType type,
        final String token,
        final DeviceType device,
        final String ipAddress,
        final Instant issuedAt,
        final Instant expiresAt
    ) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.token = token;
        this.device = device;
        this.ipAddress = ipAddress;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}

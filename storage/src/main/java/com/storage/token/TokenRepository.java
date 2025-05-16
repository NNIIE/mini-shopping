package com.storage.token;

import com.storage.enums.DeviceType;
import com.storage.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserIdAndTokenAndType(Long userId, String token, TokenType type);

    void deleteByUserIdAndTypeAndDevice(Long user_id, TokenType type, DeviceType device);
}

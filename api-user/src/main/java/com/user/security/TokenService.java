package com.user.security;

import com.storage.enums.DeviceType;
import com.storage.token.Token;
import com.storage.token.TokenRepository;
import com.storage.enums.TokenType;
import com.storage.user.User;
import com.user.global.exception.BadRequestException;
import com.user.global.exception.ErrorCode;
import com.user.global.exception.NotFoundException;
import com.user.web.response.UserTokenPairDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public void validateJwt(final String token) {
        validateJwtFormat(token);
        validateJwtExpiration(token);
    }

    public void validateJwtFormat(final String token) {
        if (!jwtTokenProvider.isParsable(token)) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
    }

    public void validateJwtExpiration(final String token) {
        final Date expiration = jwtTokenProvider.getExpiration(token);
        if (expiration == null || expiration.before(new Date())) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }
    }

    public Long validateAccessTokenAndGetUserId(final String accessToken) {
        validateJwt(accessToken);
        final Long userId = jwtTokenProvider.getClaim(accessToken, "id", Long.class);

        if (userId == null) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }

        return userId;
    }

    public Token validateAndGetRefreshToken(final String refreshToken) {
        validateJwtFormat(refreshToken);
        final Long userId = jwtTokenProvider.getClaim(refreshToken, "id", Long.class);

        if (userId == null) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }

        final Token token = tokenRepository.findByUserIdAndTokenAndType(userId, refreshToken, TokenType.REFRESH)
            .orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_TOKEN));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException(ErrorCode.INVALID_TOKEN);
        }

        return token;
    }

    public String extractAndValidateToken(final HttpServletRequest request) {
        return jwtTokenProvider.resolveToken(request);
    }

    public UserTokenPairDto createTokenPair(
        final Long userId,
        final Instant issuedAt
    ) {
        return new UserTokenPairDto(
            jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt),
            jwtTokenProvider.generateToken(TokenType.REFRESH, userId, issuedAt)
        );
    }

    public void issueRefreshToken(
        final User user,
        final String refreshToken,
        final DeviceType device,
        final String ipAddress,
        final Instant issuedAt
    ) {
        tokenRepository.deleteByUserIdAndTypeAndDevice(user.getId(), TokenType.REFRESH, device);

        final Token token = Token.builder()
            .user(user)
            .type(TokenType.REFRESH)
            .token(refreshToken)
            .device(device)
            .ipAddress(ipAddress)
            .issuedAt(issuedAt)
            .expiresAt(issuedAt.plusMillis(TokenType.REFRESH.getExpiredMs()))
            .build();

        tokenRepository.save(token);
    }

    public void rotateRefreshToken(
        final Token oldToken,
        final String newRefreshToken,
        final DeviceType device,
        final String ipAddress,
        final Instant issuedAt
    ) {
        tokenRepository.delete(oldToken);

        Token token = Token.builder()
            .user(oldToken.getUser())
            .type(TokenType.REFRESH)
            .token(newRefreshToken)
            .device(device)
            .ipAddress(ipAddress)
            .issuedAt(issuedAt)
            .expiresAt(issuedAt.plusMillis(TokenType.REFRESH.getExpiredMs()))
            .build();

        tokenRepository.save(token);
    }
}

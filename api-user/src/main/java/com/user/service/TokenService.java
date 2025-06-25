package com.user.service;

import com.relation.enums.TokenType;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import com.user.jwt.JwtTokenProvider;
import com.user.web.response.auth.UserTokenDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    public void validateJwt(final String token) {
        validateJwtFormat(token);
        validateJwtExpiration(token);
    }

    public void validateJwtFormat(final String token) {
        if (!jwtTokenProvider.isParsable(token)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    public void validateJwtExpiration(final String token) {
        final Date expiration = jwtTokenProvider.getExpiration(token);

        if (expiration == null || expiration.before(new Date())) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    public Long validateAccessTokenAndGetUserId(final String accessToken) {
        validateJwt(accessToken);
        final Long userId = jwtTokenProvider.getClaim(accessToken, "id", Long.class);

        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return userId;
    }

    public Long validateTokenAndGetUserId(final String refreshToken) {
        validateJwtFormat(refreshToken);
        validateJwtExpiration(refreshToken);

        final Long userId = jwtTokenProvider.getClaim(refreshToken, "id", Long.class);

        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        return userId;
    }

    public String extractAndValidateToken(final HttpServletRequest request) {
        return jwtTokenProvider.resolveToken(request);
    }

    public UserTokenDto createAccessAndRefreshToken(
        final Long userId,
        final Instant issuedAt
    ) {
        return new UserTokenDto(
            jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt),
            jwtTokenProvider.generateToken(TokenType.REFRESH, userId, issuedAt)
        );
    }

    public String createAccessToken(
        final Long userId,
        final Instant issuedAt
    ) {
        return jwtTokenProvider.generateToken(TokenType.ACCESS, userId, issuedAt);
    }

}

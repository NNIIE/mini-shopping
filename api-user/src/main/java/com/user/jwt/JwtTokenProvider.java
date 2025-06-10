package com.user.jwt;

import com.storage.enums.TokenType;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(
        @Value("${jwt.secret}") final String secret,
        @Value("${jwt.access-token.expire}") final Duration accessExpire,
        @Value("${jwt.refresh-token.expire}") final Duration refreshExpire
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        TokenType.ACCESS.setExpiredMs(accessExpire.toMillis());
        TokenType.REFRESH.setExpiredMs(refreshExpire.toMillis());
    }

    public <T> T getClaim(
        final String token,
        final String claimName,
        final Class<T> requiredType
    ) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get(claimName, requiredType);
    }

    public boolean isParsable(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String generateToken(
        final TokenType tokenType,
        final Long id,
        final Instant issuedAt
    ) {
        return Jwts.builder()
            .subject(String.valueOf(tokenType))
            .claim("id", id)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(issuedAt.plusMillis(tokenType.getExpiredMs())))
            .signWith(secretKey)
            .compact();
    }

    public String resolveToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }

        return "";
    }

    /**
     * JWT 토큰에서 만료 일자를 추출합니다.
     *
     * @param token 만료 일자를 확인할 JWT 토큰 문자열
     * @return 토큰의 만료 일자
     */
    public Date getExpiration(final String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

}


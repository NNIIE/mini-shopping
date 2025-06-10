package com.user.jwt;

import com.user.security.CustomUserDetailService;
import com.user.security.CustomUserDetails;
import com.user.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String token = tokenService.extractAndValidateToken(request);

        if (StringUtils.hasText(token)) {
            final Long userId = tokenService.validateAccessTokenAndGetUserId(token);
            final CustomUserDetails userDetails = getUserDetails(userId);
            final UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(userDetails);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private CustomUserDetails getUserDetails(final Long userId) {
        return (CustomUserDetails) customUserDetailService.loadUserByUsername(userId.toString());
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(final CustomUserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}


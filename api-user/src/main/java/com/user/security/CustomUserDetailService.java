package com.user.security;

import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
            final User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            return new CustomUserDetails(user);
    }

}

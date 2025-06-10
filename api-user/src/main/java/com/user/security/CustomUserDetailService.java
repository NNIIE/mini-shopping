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

    /**
     * 주어진 사용자 ID로 사용자를 조회하여 UserDetails 객체를 반환합니다.
     *
     * @param userId 조회할 사용자의 ID 문자열
     * @return 조회된 사용자의 UserDetails 객체
     * @throws BusinessException 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
            final User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            return new CustomUserDetails(user);
    }

}


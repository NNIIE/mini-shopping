package com.admin.security;

import com.admin.exception.BusinessException;
import com.admin.exception.ErrorCode;
import com.storage.admin.Admin;
import com.storage.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAdminDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    /**
     * 주어진 이메일로 관리자를 조회하여 인증에 필요한 사용자 정보를 반환합니다.
     *
     * @param email 인증에 사용할 관리자 이메일
     * @return 조회된 관리자의 사용자 정보
     * @throws BusinessException 관리자를 찾을 수 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new CustomAdminDetails(admin);
    }

}


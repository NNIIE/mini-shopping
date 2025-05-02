package com.shopping.service;

import com.shopping.storage.account.Account;
import com.shopping.storage.admin.Admin;
import com.shopping.storage.admin.AdminRepository;
import com.shopping.web.request.AdminSignUpRequest;
import com.shopping.web.response.AdminSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AccountService accountService;
    private final AdminRepository adminRepository;

    @Transactional
    public AdminSignUpResponse adminSignUp(final AdminSignUpRequest request) {
        final Account createAccount = accountService.createAdminAccount(request.getEmail(), request.getPassword());
        final Admin admin = new Admin(createAccount);
        final Admin signUpAdmin = adminRepository.save(admin);

        return new AdminSignUpResponse(
            signUpAdmin.getId(),
            signUpAdmin.getAccount().getEmail()
        );
    }

}

package com.admin.integration;

import com.admin.fixture.AdminFixture;
import com.admin.global.exception.ConflictException;
import com.admin.global.exception.ErrorCode;
import com.admin.service.PasswordEncoder;
import com.admin.web.request.AdminSignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.admin.Admin;
import com.storage.admin.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@Transactional
class AdminSignUpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("관리자 등록 성공 - 201 Created 응답")
    void signUp_201Created() throws Exception {
        int a= 0;
        // given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();

        // when
        ResultActions result = mockMvc.perform(
            post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );


        Account savedAccount = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ConflictException(ErrorCode.USER_NOT_FOUND));

        Admin savedAdmin = adminRepository.findById(savedAccount.getId())
            .orElseThrow(() -> new ConflictException(ErrorCode.USER_NOT_FOUND));

        // then
        result.andDo(print())
            .andExpect(status().isCreated());

        assertAll(
            () -> assertThat(savedAccount).isEqualTo(savedAdmin.getAccount()),
            () -> assertThat(passwordEncoder.verifyPassword(request.getPassword(), savedAccount.getPassword())).isTrue()
        );
    }

    @Test
    @DisplayName("관리자 등록 실패 - 중복된 이메일")
    void signUp_ExistEmail() throws Exception {
        // given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();
        AdminSignUpRequest signUpRequestExistEmail = AdminFixture.createRequestForAdminSignUpParameter(
            "admintest@email.com",
            "Qwer1234!!"
        );

        // when
        mockMvc.perform(
            post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        mockMvc.perform(
                post("/admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequestExistEmail)))
            .andExpect(status().isConflict());
    }

}

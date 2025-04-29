package com.shopping.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.fixture.UserFixture;
import com.shopping.global.exception.ErrorCode;
import com.shopping.global.exception.NotFoundException;
import com.shopping.service.PasswordEncoder;
import com.shopping.storage.entity.Account;
import com.shopping.storage.entity.Admin;
import com.shopping.storage.repository.AccountRepository;
import com.shopping.storage.repository.AdminRepository;
import com.shopping.web.request.AdminSignUpRequest;
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
public class AdminSignUpIntegrationTest {

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
        // given
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();

        // when
        ResultActions result = mockMvc.perform(
            post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        Account savedAccount = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Admin savedAdmin = adminRepository.findById(savedAccount.getId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

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
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();
        AdminSignUpRequest signUpRequestExistEmail = UserFixture.createRequestForAdminSignUpParameter(
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

package com.user.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relation.account.Account;
import com.relation.account.AccountRepository;
import com.relation.user.User;
import com.relation.user.UserRepository;
import com.user.exception.BusinessException;
import com.user.exception.ErrorCode;
import com.user.service.PasswordEncoder;
import com.user.web.request.auth.UserSignUpRequest;
import org.junit.jupiter.api.DisplayName;
import com.user.fixture.UserFixture;
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
class UserSignUpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 - 201 Created 응답")
    void signUp_201Created() throws Exception {
        // given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();

        // when
        ResultActions result = mockMvc.perform(
            post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        Account savedAccount = accountRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        User savedUser = userRepository.findById(savedAccount.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // then
        result.andDo(print())
            .andExpect(status().isCreated());

        assertAll(
            () -> assertThat(savedAccount).isEqualTo(savedUser.getAccount()),
            () -> assertThat(savedUser.getNickname()).isEqualTo(request.getNickname()),
            () -> assertThat(passwordEncoder.verifyPassword(request.getPassword(), savedAccount.getPassword())).isTrue()
        );
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signUp_ExistEmail() throws Exception {
        // given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();
        UserSignUpRequest requestExistEmail = UserFixture.createRequestForUserSignUpParameter(
            "usertest@email.com",
            "testUserA",
            "Qwer1234!!",
            "010-1234-5678"
        );

        // when
        mockMvc.perform(
            post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        mockMvc.perform(
                post("/user/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestExistEmail)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    void signUp_ExistNickname() throws Exception {
        // given
        UserSignUpRequest request = UserFixture.createRequestForUserSignUp();
        UserSignUpRequest requestExistNickname = UserFixture.createRequestForUserSignUpParameter(
            "testA@email.com",
            "testUser",
            "Qwer1234!!",
            "010-1234-5678"
        );

        // when
        mockMvc.perform(
            post("/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        mockMvc.perform(
                post("/user/signUp")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestExistNickname)))
            .andExpect(status().isInternalServerError());
    }

}

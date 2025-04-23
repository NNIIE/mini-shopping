package com.shopping.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.fixture.UserFixture;
import com.shopping.model.UserRole;
import com.shopping.model.UserStatus;
import com.shopping.model.entity.User;
import com.shopping.model.request.UserSignUpRequest;
import com.shopping.repository.UserRepository;
import com.shopping.service.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@Transactional
class UserSignUpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 - 201 Created 응답")
    void signUp_201Created() throws Exception {
        // given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();

        // when
        ResultActions result = mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        User savedUser = userRepository.findByEmail(signUpRequest.getEmail());

        // then
        result.andDo(print())
            .andExpect(status().isCreated());

        assertAll(
            () -> assertThat(savedUser.getEmail()).isEqualTo(signUpRequest.getEmail()),
            () -> assertThat(savedUser.getNickname()).isEqualTo(signUpRequest.getNickname()),
            () -> assertThat(passwordEncoder.verifyPassword(signUpRequest.getPassword(), savedUser.getPassword())).isTrue(),
            () -> assertThat(savedUser.getRole()).isEqualTo(UserRole.BASIC),
            () -> assertThat(savedUser.getStatus()).isEqualTo(UserStatus.ACTIVE)
        );
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    void signUp_ExistEmail() throws Exception {
        // given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        UserSignUpRequest signUpRequestExistEmail = UserFixture.createSignUpRequestByParameter(
            "test@email.com",
            "testUserA",
            "Qwer1234!!",
            UserRole.BASIC
        );

        // when
        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        // then
        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestExistEmail)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    void signUp_ExistNickname() throws Exception {
        // given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        UserSignUpRequest signUpRequestExistNickname = UserFixture.createSignUpRequestByParameter(
            "testA@email.com",
            "testUser",
            "Qwer1234!!",
            UserRole.BASIC
        );

        // when
        mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        // then
        mockMvc.perform(
                post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signUpRequestExistNickname)))
            .andExpect(status().isConflict());
    }

}

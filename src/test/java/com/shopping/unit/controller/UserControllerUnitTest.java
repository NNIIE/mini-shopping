package com.shopping.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.config.SecurityConfig;
import com.shopping.controller.UserController;
import com.shopping.fixture.UserFixture;
import com.shopping.model.request.UserSignUpRequest;
import com.shopping.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 - 잘못된 이메일 형식")
    void invalidEmail() throws Exception {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        ReflectionTestUtils.setField(signUpRequest, "email", "userEmail");

        // When & Then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 - 잘못된 닉네임 형식")
    void invalidNickname() throws Exception {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        ReflectionTestUtils.setField(signUpRequest, "nickname", "12341234");

        // When & Then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 - 잘못된 비밀번호 형식")
    void invalidPassword() throws Exception {
        // Given
        UserSignUpRequest signUpRequest = UserFixture.createSignUpRequest();
        ReflectionTestUtils.setField(signUpRequest, "password", "12341234");

        // When & Then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest());
    }

}

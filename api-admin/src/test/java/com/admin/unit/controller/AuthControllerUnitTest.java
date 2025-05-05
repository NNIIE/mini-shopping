package com.admin.unit.controller;

import com.admin.fixture.AdminFixture;
import com.admin.global.config.SecurityConfig;
import com.admin.service.AuthService;
import com.admin.web.controller.AuthController;
import com.admin.web.request.AdminSignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("관리자 등록 - 잘못된 이메일 형식")
    void invalidEmail() throws Exception {
        // Given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();
        ReflectionTestUtils.setField(request, "email", "adminEmail");

        // When & Then
        mockMvc.perform(post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("관리자 등록 - 잘못된 비밀번호 형식")
    void invalidPassword() throws Exception {
        // Given
        AdminSignUpRequest request = AdminFixture.createRequestForAdminSignUp();
        ReflectionTestUtils.setField(request, "password", "12341234");

        // When & Then
        mockMvc.perform(post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

}
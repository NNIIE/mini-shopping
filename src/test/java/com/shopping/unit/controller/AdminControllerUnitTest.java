package com.shopping.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.fixture.UserFixture;
import com.shopping.global.config.SecurityConfig;
import com.shopping.service.AdminService;
import com.shopping.web.controller.AdminController;
import com.shopping.web.request.AdminSignUpRequest;
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
@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    @Test
    @DisplayName("관리자 등록 - 잘못된 이메일 형식")
    void invalidEmail() throws Exception {
        // Given
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();
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
        AdminSignUpRequest request = UserFixture.createRequestForAdminSignUp();
        ReflectionTestUtils.setField(request, "password", "12341234");

        // When & Then
        mockMvc.perform(post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

}

package com.admin.integration;

import com.admin.fixture.AdminFixture;
import com.admin.web.request.AdminSignInRequest;
import com.admin.web.request.AdminSignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@Transactional
class AdminSignInIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("관리자 로그인 성공")
    void adminSignIn_success_jsessionid_issued() throws Exception {
        // given
        AdminSignUpRequest signUpRequest = AdminFixture.createRequestForAdminSignUp();
        mockMvc.perform(
            post("/admin/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        ).andExpect(status().isCreated());

        // when
        AdminSignInRequest signInRequest = AdminFixture.createRequestForAdminSignIn(
            signUpRequest.getEmail(),
            signUpRequest.getPassword()
        );

        mockMvc.perform(
            post("/admin/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest))
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @DisplayName("로그인 실패")
    void adminSignIn_fail_wrong_password() throws Exception {
        // given
        AdminSignUpRequest signUpRequest = AdminFixture.createRequestForAdminSignUp();
        mockMvc.perform(
            post("/admin/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        ).andExpect(status().isCreated());

        // when
        AdminSignInRequest signInRequest = AdminFixture.createRequestForAdminSignIn(
            signUpRequest.getEmail(),
            "qwer1234@@" // 잘못된 비밀번호
        );

        mockMvc.perform(
            post("/admin/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest))
            )
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

}

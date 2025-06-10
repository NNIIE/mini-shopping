package com.user.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storage.account.Account;
import com.storage.account.AccountRepository;
import com.storage.enums.AccountStatus;
import com.storage.enums.UserRole;
import com.storage.user.User;
import com.storage.user.UserRepository;
import com.user.fixture.TokenFixture;
import com.user.fixture.UserFixture;
import com.user.web.request.auth.ReissueTokenRequest;
import com.user.web.request.auth.UserSignInRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@Transactional
class UserSignInIntegrationTest {

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

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "Qwer1234!!";

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
            .email(TEST_EMAIL)
            .password(passwordEncoder.encode(TEST_PASSWORD))
            .role(UserRole.BASIC)
            .status(AccountStatus.ACTIVE)
            .build();

        accountRepository.save(account);

        User testUser = User.builder()
            .account(account)
            .nickname("testUser")
            .phoneNumber("01012345678")
            .build();

        userRepository.save(testUser);
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn_WithValidCredentialss() throws Exception {
        // given
        UserSignInRequest request = UserFixture.createRequestForUserSignInParameter(TEST_EMAIL, TEST_PASSWORD);

        // when & then
        mockMvc.perform(post("/user/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일")
    void signIn_WithInvalidEmail() throws Exception {
        // given
        UserSignInRequest request = UserFixture.createRequestForUserSignInParameter("wrong@example.com", TEST_PASSWORD);

        // when & then
        mockMvc.perform(post("/user/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호")
    void signIn_WithInvalidPassword() throws Exception {
        // given
        UserSignInRequest request = UserFixture.createRequestForUserSignInParameter(TEST_EMAIL, "Qwer1234!!!");

        // when & then
        mockMvc.perform(post("/user/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("토큰 갱신 성공")
    void reissueToken_WithValidRefreshToken() throws Exception {
        // given
        UserSignInRequest signInRequest = UserFixture.createRequestForUserSignInParameter(TEST_EMAIL, TEST_PASSWORD);

        MvcResult signInResult = mockMvc.perform(post("/user/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequest)))
            .andExpect(status().isOk())
            .andReturn();

        String responseContent = signInResult.getResponse().getContentAsString();
        String refreshToken = new JSONObject(responseContent).getString("refreshToken");
        ReissueTokenRequest reissueRequest = TokenFixture.createRequestForReissueToken(refreshToken);

        // when & then
        mockMvc.perform(post("/user/reissueToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reissueRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
            .andExpect(content().string(containsString("eyJhbGciOiJ")));
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 리프레시 토큰")
    void reissueToken_WithInvalidRefreshToken_ShouldReturnBadRequest() throws Exception {
        // given
        ReissueTokenRequest reissueRequest = TokenFixture.createRequestForReissueToken("invalid.refresh.token");

        // when & then
        mockMvc.perform(post("/user/reissueToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reissueRequest)))
            .andExpect(status().isInternalServerError());
    }

}

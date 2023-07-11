package com.toycode.study.security.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.toycode.study.security.application.port.dto.LoginRequest;
import com.toycode.study.security.application.port.dto.LoginResponse;
import com.toycode.study.security.application.port.in.LoginUseCase;
import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.TokenInfo.Token;
import com.toycode.study.security.domain.User.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest extends AbstractControllerTest {

    @MockBean
    private LoginUseCase loginUseCase;

    @Value("${app.api.version}")
    private String DEFAULT_URL;

    @Test
    @DisplayName("사용자 로그인")
    void login_user() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("dream", "dream1004");

        TokenInfo tokenInfo = new TokenInfo(
            Token.of("sample_token"),
            Username.of("dream"),
            false, false);

        LoginResponse token = LoginResponse.of(tokenInfo);
        BDDMockito.given(loginUseCase.login(request)).willReturn(token);

        // When
        mockMvc.perform(
                post(DEFAULT_URL + AuthenticationController.DOMAIN + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(resp ->
                Assertions.assertEquals(
                    objectMapper.writeValueAsString(token),
                    resp.getResponse().getContentAsString()));
    }
}
package com.toycode.study.security.adapter.in.web;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.in.UserRegistrationUseCase;
import com.toycode.study.security.domain.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends AbstractControllerTest {

    @MockBean
    private UserRegistrationUseCase userRegistrationUseCase;

    // TODO UserDeleteUseCase 작성해야함.

    @Value("${app.api.version}")
    private String DEFAULT_URL;

    @ParameterizedTest
    @ValueSource(strings = {
        // success
        "{\"username\":\"dream\",\"nickname\":\"최고 관리자\",\"password\":\"dream1004\",\"authority\":\"ROOT_MANAGER\" }"
    })
    @DisplayName("'사용자 등록 요청' 호출 확인")
    void verify_api_call(String body) throws Exception {
        mockMvc.perform(
                post(DEFAULT_URL + UserController.DOMAIN + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        then(userRegistrationUseCase).should()
            .registerUser(eq(new UserRegistrationRequest(
                "dream",
                "최고 관리자",
                "dream1004",
                Authority.ROOT_MANAGER
            )));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        // 정규식
        "{\"username\":\"Dream@\",\"nickname\":\"최고 관리자\",\"password\":\"dream1004\",\"authority\":\"manager\"}",
        // NotBlank
        "{\"username\":\"dream\",\"nickname\":\"최고 관리자\",\"password\":\"\",\"authority\":\"manager\"}",
        // deserialized error
        "{\"username\":\"dream\",\"nickname\":\"최고 관리자\",\"authority\":\"manager\"}"
    })
    @DisplayName("'사용자 등록 요청문' 검증")
    void verify_api_params(String body) throws Exception {
        mockMvc.perform(
                post(DEFAULT_URL + UserController.DOMAIN + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest());
    }
}
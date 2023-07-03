package com.toycode.study.security.application.port.in.dto;

import com.toycode.study.security.domain.Authority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * `사용자 등록 요청문`으로 계층간의 데이터 전송을 목적한다.
 */
@Data
public class UserRegistrationRequest {

    @Pattern(
        regexp = "^[a-zA-Z0-9]{4,16}$",
        message = "`username`은 영문 대소문자와 숫자로만 이루어진 4~16자여야 합니다.")
    @NotBlank(message = "`username`을 입력해주세요.")
    private final String username;

    @NotBlank(message = "`nickname`을 입력해주세요.")
    private final String nickname;

    @NotBlank(message = "`password`을 입력해주세요.")
    private final String password;

    @NotNull(message = "`authority`를 선택해주세요.")
    private final Authority authority;
}

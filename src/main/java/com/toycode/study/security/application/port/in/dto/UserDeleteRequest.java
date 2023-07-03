package com.toycode.study.security.application.port.in.dto;

import com.toycode.study.security.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * `사용자 삭제 요청문`으로 계층간의 데이터 전송을 목적한다.
 */
@Data
public class UserDeleteRequest {

    @NotBlank
    private final User.Username username;
}

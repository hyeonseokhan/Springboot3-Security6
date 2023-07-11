package com.toycode.study.security.application.port.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * '사용자 삭제 요청문'으로 계층간의 데이터 전송을 목적한다.
 */
@Data
public class UserDeleteRequest {

    @NotBlank(message = "'username' 을 입력해주세요.")
    private final String username;
}

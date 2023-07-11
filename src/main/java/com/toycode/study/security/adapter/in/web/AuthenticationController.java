package com.toycode.study.security.adapter.in.web;

import com.toycode.study.security.application.port.dto.LoginRequest;
import com.toycode.study.security.application.port.dto.LoginResponse;
import com.toycode.study.security.application.port.in.LoginUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "인증/인가 컨트롤러")
@RestController
@RequestMapping("${app.api.version}" + AuthenticationController.DOMAIN)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class AuthenticationController {

    public static final String DOMAIN = "/auth";

    private final LoginUseCase loginUseCase;

    /**
     * 사용자 로그인 인증을 통해 토큰을 발급
     *
     * @param request 사용자 로그인 정보
     * @return 발급된 토큰(JWT)
     */
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(
        @Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.login(request));
    }
}

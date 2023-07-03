package com.toycode.study.security.adapter.in.web;

import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.in.UserRegistrationUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 컨트롤러")
@RestController
@RequestMapping("${app.api.version}" + UserController.DOMAIN)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class UserController {

    public static final String DOMAIN = "/user";

    private final UserRegistrationUseCase userRegistrationUseCase;

    /**
     * 신규 사용자 등록
     *
     * @param request 사용자 등록 요청문
     * @접근권한 '최고 관리자'
     */
    @PostMapping("/register")
    ResponseEntity<Void> register(
        @Valid @RequestBody final UserRegistrationRequest request) {
        userRegistrationUseCase.registerUser(request);
        return ResponseEntity.ok().build();
    }
}

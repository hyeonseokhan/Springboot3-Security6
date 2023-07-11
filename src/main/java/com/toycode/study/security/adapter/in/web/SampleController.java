package com.toycode.study.security.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sample", description = "샘플요청 컨트롤러")
@RestController
@RequestMapping("${app.api.version}" + SampleController.DOMAIN)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class SampleController {

    public static final String DOMAIN = "/rest";

    @GetMapping("/user")
    ResponseEntity<String> findUser() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/do/{command}")
    @PreAuthorize("hasAnyAuthority('ROOT_MANAGER')")
    ResponseEntity<String> doSomething(@PathVariable String command) {
        return ResponseEntity.ok(command);
    }
}

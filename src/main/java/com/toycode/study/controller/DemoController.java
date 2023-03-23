package com.toycode.study.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("get")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Hello");
    }
}

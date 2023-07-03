package com.toycode.study.security.adapter.in.web;

import java.io.File;
import lombok.Builder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

abstract class AbstractController {

    private static final String SUCCESS = "success";

    protected ResponseEntity<Void> createRespMsg() {
        return ResponseEntity.ok().build();
    }

    protected ResponseEntity<?> createRespMsg(Object data) {
        return ResponseEntity.ok().body(
            Response.builder()
                .result(SUCCESS)
                .data(data)
                .build()
        );
    }

    protected ResponseEntity<?> createRespMsg(String data, MediaType mediaType) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
            .body(data);
    }

    protected ResponseEntity<Resource> createRespMsg(File file) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "filename=" + file.getName())
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
            .body(new FileSystemResource(file));
    }

    @Builder
    private static class Response {

        private String result;
        private Object data;
    }
}

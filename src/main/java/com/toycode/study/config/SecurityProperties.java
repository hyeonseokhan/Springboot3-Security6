package com.toycode.study.config;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = SecurityProperties.SECURITY_PREFIX)
public class SecurityProperties {

    public static final String SECURITY_PREFIX = "app.security";

    private Include include = new Include();
    private Exclude exclude = new Exclude();

    /**
     * 포함할 경로들
     */
    @Data
    @NoArgsConstructor
    public static class Include {

        /**
         * CORS 경로들
         */
        protected List<String> corsPaths;
        /**
         * IP 기반 경로들
         */
        protected List<IpPaths> ipPaths;
        /**
         * API 경로들
         */
        protected List<List<String>> apiPaths;
    }

    /**
     * 제외할 경로들
     */
    @Data
    @NoArgsConstructor
    public static class Exclude {

        /**
         * 웹 경로들
         */
        protected List<String> webPaths;
        /**
         * CSRF 경로들
         */
        protected List<String> csrfPaths;
        /**
         * API 경로들
         */
        protected List<List<String>> apiPaths;
    }

    /**
     * IP 기반 경로
     */
    @Data
    @NoArgsConstructor
    public static class IpPaths {

        /**
         * 경로들
         */
        protected String path;
        /**
         * IP들
         */
        protected List<String> ips;
    }
}

package com.toycode.study.security.config;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = SecurityProperties.SECURITY_PREFIX)
public class SecurityProperties implements InitializingBean {

    public static final String SECURITY_PREFIX = "app.security";

    private Exclude exclude = new Exclude();

    @Override
    public void afterPropertiesSet() {
        Assert.notEmpty(this.exclude.apiPaths, "제외할 기본 api 경로가 없습니다.");
    }

    /**
     * 제외할 경로들
     */
    @Data
    @NoArgsConstructor
    public static class Exclude {

        /**
         * API 경로들
         */
        private List<String> apiPaths;
    }
}

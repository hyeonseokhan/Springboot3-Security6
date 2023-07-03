package com.toycode.study.security.config;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    prefix = H2DBConfig.CONDITION_PROPERTIY_PREFIX,
    name = "enabled",
    havingValue = "true")
class H2DBConfig {

    public static final String CONDITION_PROPERTIY_PREFIX = "spring.h2.console";
    private static final String H2_PROPERTIES_PREFIX = "spring.datasource.hikari";

    @Bean
    @ConfigurationProperties(prefix = H2DBConfig.H2_PROPERTIES_PREFIX)
    public DataSource dataSource() throws SQLException {
        Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        return DataSourceBuilder.create().build();
    }
}
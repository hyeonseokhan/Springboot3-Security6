package com.toycode.study.config;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2DBConfig {

    private static final String H2_PROPERTIES_PREFIX = "spring.datasource.hikari";

    @Bean
    @ConfigurationProperties(prefix = H2DBConfig.H2_PROPERTIES_PREFIX)
    public DataSource dataSource() throws SQLException {
        Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        return DataSourceBuilder.create().build();
    }
}

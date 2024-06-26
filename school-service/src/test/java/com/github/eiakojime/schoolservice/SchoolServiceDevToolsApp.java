package com.github.eiakojime.schoolservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;


@Configuration
public class SchoolServiceDevToolsApp {
    @Bean
    @RestartScope
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine");
    }

    public static void main(String[] args) {
        SpringApplication.
                from(SchoolService::main)
                .with(SchoolServiceDevToolsApp.class)
                .run(args);
    }
}

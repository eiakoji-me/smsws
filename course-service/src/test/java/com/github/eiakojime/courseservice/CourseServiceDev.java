package com.github.eiakojime.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class CourseServiceDev {
    @Bean
    @RestartScope
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine");
    }

    public static void main(String[] args) {
        SpringApplication.
                from(CourseService::main)
                .with(CourseServiceDev.class)
                .run(args);
    }
}

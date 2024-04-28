package com.github.eiakojime.configservice;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigServiceDevToolsApp {
    public static void main(String[] args) {
        SpringApplication
                .from(ConfigService::main)
                .with(ConfigServiceDevToolsApp.class)
                .run(args);
    }
}

package com.school.courseservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.school.courseservice.repository")
public class R2dbcConfig {
}

package com.ssa.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomNamingStrategyConfig {

    @Bean
    @ConditionalOnExpression("${custom.naming.strategy.enabled:true}")
    public com.ssa.config.CustomPhysicalNamingStrategy customPhysicalNamingStrategy() {
        return new com.ssa.config.CustomPhysicalNamingStrategy();
    }
}
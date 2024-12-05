package com.ssa.config;

import jakarta.persistence.Entity;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties
public class HibernateConfig {

    private final Environment environment;

    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, PhysicalNamingStrategyStandardImpl.class.getName());
            hibernateProperties.put(AvailableSettings.DIALECT, environment.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQLDialect"));
            // Set default properties
            String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto", "update");
            hibernateProperties.put("hibernate.hbm2ddl.auto", ddlAuto);
        };
    }

    @Bean
    public Set<Class<?>> entities(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return Objects.requireNonNull(entityManagerFactory.getPersistenceUnitInfo()).getManagedClassNames().stream().map(className -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(clazz -> clazz != null && clazz.isAnnotationPresent(Entity.class)).collect(Collectors.toSet());
    }
}
package com.legmod.ddd.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.integration.monitor.IntegrationMBeanExporter;

/**
 * Configuration for Spring Integration to avoid warnings about bean order for BeanPostProcessor.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IntegrationConfig {
    @Bean
    public static IntegrationMBeanExporter integrationMbeanExporter() {
        return new IntegrationMBeanExporter();
    }
}

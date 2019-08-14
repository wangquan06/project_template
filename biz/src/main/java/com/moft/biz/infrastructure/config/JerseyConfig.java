package com.moft.biz.infrastructure.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@Configuration
@ApplicationPath("rest")
public class JerseyConfig extends ResourceConfig {
    /**
     * 扫描com.makeronly包，使其识别JAX-RS注解
     */
    public JerseyConfig() {
        packages("com.moft.biz.interfaces.facade");
    }
}

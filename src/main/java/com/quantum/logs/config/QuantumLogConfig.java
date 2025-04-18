package com.quantum.logs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.quantum.logs.beans.QuantumLogInterceptor;

@Configuration
public class QuantumLogConfig implements WebMvcConfigurer {

    @Autowired
    private QuantumLogInterceptor quantumLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(quantumLogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/error/**")
                .order(1);
    }
}

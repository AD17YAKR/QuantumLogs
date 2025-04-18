package com.quantum.logs.config;

import com.quantum.logs.beans.QuantumLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuantumLogConfig {

    @Bean
    public FilterRegistrationBean<QuantumLogFilter> quantumLogFilter() {
        FilterRegistrationBean<QuantumLogFilter> registrationBean = new FilterRegistrationBean<>();

        // Create filter with the main application class
        QuantumLogFilter quantumFilter = new QuantumLogFilter(getApplicationClass());

        registrationBean.setFilter(quantumFilter);
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        registrationBean.setOrder(1); // Set filter order if needed

        return registrationBean;
    }

    private Class<?> getApplicationClass() {
        try {
            // Replace with your main application class path
            return Class.forName("com.quantum.logs.LogsApplication");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find main application class", e);
        }
    }
}

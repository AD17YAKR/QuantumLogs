package com.quantum.logs.config;

import com.quantum.logs.annotations.EnableQuantumLog;
import com.quantum.logs.beans.QuantumLogInterceptor;
import com.quantum.logs.utils.IQuantumLogPrinter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
@ConditionalOnBean(annotation = EnableQuantumLog.class)
public class QuantumLogAutoConfiguration implements ImportAware, WebMvcConfigurer {

    private boolean enabled = true;
    private boolean printAll = false;
    private boolean enableCurls = false;
    private final IQuantumLogPrinter logPrinter;

    public QuantumLogAutoConfiguration(IQuantumLogPrinter logPrinter) {
        this.logPrinter = logPrinter;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> attrs = importMetadata
                .getAnnotationAttributes(EnableQuantumLog.class.getName());
        if (attrs != null) {
            this.enabled = (Boolean) attrs.get("enabled");
            this.printAll = (Boolean) attrs.get("printAll");
            this.enableCurls = (Boolean) attrs.get("enableCurls");
        }
    }

    @Bean
    public QuantumLogInterceptor quantumLogInterceptor() {
        return new QuantumLogInterceptor(logPrinter, enabled, printAll, enableCurls);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(quantumLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/error/**")
                .order(1);
    }
}

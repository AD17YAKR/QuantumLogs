package com.quantum.logs.beans;

import com.quantum.logs.annotations.EnableQuantumLog;
import com.quantum.logs.annotations.ExcludeFromQuantumLog;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import org.springframework.core.annotation.AnnotationUtils;

public class QuantumLogFilter implements Filter {
    private final Class<?> applicationClass;
    private final boolean enabled;

    public QuantumLogFilter(Class<?> applicationClass) {
        this.enabled = AnnotationUtils.findAnnotation(applicationClass, EnableQuantumLog.class) != null;
        System.out.println(
                "QuantumLogFilter initialized with enabled=" + enabled + " for class=" + applicationClass.getName());
        this.applicationClass = applicationClass;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!enabled) {
            return;
        }
        if (request instanceof HttpServletRequest httpRequest) {
            String methodName = ((HttpServletRequest) request).getMethod();
            String requestURI = ((HttpServletRequest) request).getRequestURI();

            if (!isMethodExcluded(requestURI)) {
                System.out.println("Request Method: " + methodName);
                System.out.println("Request URI: " + requestURI);
                Enumeration<String> headerNames = httpRequest.getHeaderNames();
                System.out.println("Headers:");
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = httpRequest.getHeader(headerName);
                    System.out.println("\t" + headerName + ": " + headerValue);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isMethodExcluded(String requestURI) {
        try {
            Method method = findMethodForRequestURI(requestURI);
            if (method != null) {
                return method.isAnnotationPresent(ExcludeFromQuantumLog.class);
            }
        } catch (Exception e) {
        }
        return false;
    }

    private Method findMethodForRequestURI(String requestURI) {
        return null;
    }
}

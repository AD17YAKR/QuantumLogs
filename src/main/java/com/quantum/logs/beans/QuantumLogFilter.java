package com.quantum.logs.beans;

import com.quantum.logs.LogsApplication;
import com.quantum.logs.annotations.EnableQuantumLog;
import com.quantum.logs.annotations.ExcludeFromQuantumLog;
import com.quantum.logs.utils.IQuantumLogPrinter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class QuantumLogFilter implements HandlerInterceptor {
    private final boolean enabled;
    private final IQuantumLogPrinter logPrinter;

    public QuantumLogFilter(IQuantumLogPrinter logPrinter) {
        this.logPrinter = logPrinter;
        this.enabled = AnnotationUtils.findAnnotation(
                LogsApplication.class, EnableQuantumLog.class) != null;
        System.out.println("QuantumLogInterceptor enabled=" + enabled);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (!enabled || !(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(ExcludeFromQuantumLog.class)) {
            return true;
        }

        logPrinter.printRequestCurl(request);
        logPrinter.printRequestHeaders(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        if (!enabled || !(handler instanceof HandlerMethod)) {
            return;
        }

        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(ExcludeFromQuantumLog.class)) {
            return;
        }
        logPrinter.printResponseHeaders(response);
    }
}

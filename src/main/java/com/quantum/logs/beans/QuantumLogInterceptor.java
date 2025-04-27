// src/main/java/com/quantum/logs/beans/QuantumLogInterceptor.java
package com.quantum.logs.beans;

import com.quantum.logs.annotations.ExcludeFromQuantumLog;
import com.quantum.logs.utils.IQuantumLogPrinter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuantumLogInterceptor implements HandlerInterceptor {

    private final boolean enabled;
    private final boolean printAll;
    private final boolean enableCurls;
    private final IQuantumLogPrinter logPrinter;
    private final boolean logBodies;
    private final ConcurrentMap<String, LocalDate> lastLogged = new ConcurrentHashMap<>();

    public QuantumLogInterceptor(IQuantumLogPrinter logPrinter,
            boolean enabled,
            boolean printAll, boolean enableCurls, boolean logBodies) {
        this.logPrinter = logPrinter;
        this.enabled = enabled;
        this.printAll = printAll;
        this.enableCurls = enableCurls;
        this.logBodies = logBodies;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!enabled || !(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(ExcludeFromQuantumLog.class)) {
            return true;
        }

        String uri = request.getRequestURI();
        LocalDate today = LocalDate.now();
        boolean shouldPrint = printAll || !today.equals(lastLogged.get(uri));

        if (shouldPrint) {
            if (!printAll)
                lastLogged.put(uri, today);
            if (enableCurls) {
                logPrinter.printRequestCurl(request);
            }
            logPrinter.printRequestHeaders(request);
            if (logBodies) {
                logPrinter.printRequestBody(request);
            }
            request.setAttribute("quantumLogPrinted", true);
        } else {
            request.setAttribute("quantumLogPrinted", false);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        if (!enabled || !(handler instanceof HandlerMethod)) {
            return;
        }

        Boolean printed = (Boolean) request.getAttribute("quantumLogPrinted");
        if (Boolean.TRUE.equals(printed)) {
            logPrinter.printResponseHeaders(response);
            
            byte[] responseBody = (byte[]) request.getAttribute("responseBody");
            if (responseBody != null) {
                logPrinter.printResponseBody(responseBody);
            }
        }
    }
}
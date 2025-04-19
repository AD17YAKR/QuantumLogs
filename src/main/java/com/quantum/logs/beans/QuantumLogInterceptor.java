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
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class QuantumLogInterceptor implements HandlerInterceptor {
    private final boolean isAnnotationAdded;
    private final boolean printAll;
    private final boolean enabled;
    private final IQuantumLogPrinter logPrinter;

    private final ConcurrentMap<String, LocalDate> lastLogged = new ConcurrentHashMap<>();

    public QuantumLogInterceptor(IQuantumLogPrinter logPrinter) {
        this.logPrinter = logPrinter;
        EnableQuantumLog annotation = AnnotationUtils.findAnnotation(
                LogsApplication.class, EnableQuantumLog.class);
        this.isAnnotationAdded = annotation != null;
        this.printAll = annotation != null && annotation.printAll();
        this.enabled = annotation != null && annotation.enabled();
        System.out.println("QuantumLogInterceptor isAnnotationAdded=" + isAnnotationAdded + ", printAll=" + printAll);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (!enabled) {
            return true;
        }

        if (!isAnnotationAdded || !(handler instanceof HandlerMethod)) {
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
            if (!printAll) {
                lastLogged.put(uri, today);
            }
            logPrinter.printRequestCurl(request);
            logPrinter.printRequestHeaders(request);
            request.setAttribute("quantumLogPrinted", true);
        } else {
            request.setAttribute("quantumLogPrinted", false);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        if (!enabled) {
            return;
        }

        if (!isAnnotationAdded || !(handler instanceof HandlerMethod)) {
            return;
        }

        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(ExcludeFromQuantumLog.class)) {
            return;
        }

        Boolean printed = (Boolean) request.getAttribute("quantumLogPrinted");
        if (printed != null && printed) {
            logPrinter.printResponseHeaders(response);
        }
    }
}

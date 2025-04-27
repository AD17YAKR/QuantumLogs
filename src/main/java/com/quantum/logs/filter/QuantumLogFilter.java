package com.quantum.logs.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.quantum.logs.beans.CachedBodyHttpServletRequest;
import com.quantum.logs.beans.CachedBodyHttpServletResponse;

import java.io.IOException;

// @Component
public class QuantumLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) request);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse(
                (HttpServletResponse) response);

        request.setAttribute("cachedRequest", cachedRequest);

        chain.doFilter(cachedRequest, cachedResponse);

        request.setAttribute("responseBody", cachedResponse.getCachedBody());
    }
}
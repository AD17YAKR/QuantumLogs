package com.quantum.logs.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IQuantumLogPrinter {
    void printRequestCurl(HttpServletRequest request);

    void printRequestHeaders(HttpServletRequest request);

    void printResponseHeaders(HttpServletResponse response);
}
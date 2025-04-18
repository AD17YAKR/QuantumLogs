package com.quantum.logs.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class QuantumLogPrinter implements IQuantumLogPrinter {

    @Override
    public void printRequestCurl(HttpServletRequest request) {
        StringBuilder curl = new StringBuilder("curl -X ")
                .append(request.getMethod());

        StringBuilder url = new StringBuilder()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            url.append(":").append(request.getServerPort());
        }
        url.append(request.getRequestURI());
        if (request.getQueryString() != null) {
            url.append("?").append(request.getQueryString());
        }

        curl.append(" \"").append(url).append("\"");

        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    String value = request.getHeader(name).replace("\"", "\\\"");
                    curl.append(" -H \"")
                            .append(name)
                            .append(": ")
                            .append(value)
                            .append("\"");
                });

        System.out.println("cURL: " + curl);
    }

    @Override
    public void printRequestHeaders(HttpServletRequest request) {
        System.out.println("Request Headers:");
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    System.out.println("\t" + name + ": " + request.getHeader(name));
                });
    }

    @Override
    public void printResponseHeaders(HttpServletResponse response) {
        System.out.println("Response Headers:");
        for (String name : response.getHeaderNames()) {
            System.out.println("\t" + name + ": " + response.getHeader(name));
        }
    }
}
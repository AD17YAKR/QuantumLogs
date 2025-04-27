package com.quantum.logs.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantum.logs.beans.CachedBodyHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class QuantumLogPrinter implements IQuantumLogPrinter {

    private static final Logger logger = LoggerFactory.getLogger(QuantumLogPrinter.class);

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
        logger.debug("cURL: " + curl);
    }

    @Override
    public void printRequestHeaders(HttpServletRequest request) {
        logger.debug("Request Headers:");
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    logger.debug("\t" + name + ": " + request.getHeader(name));
                });
    }

    @Override
    public void printResponseHeaders(HttpServletResponse response) {
        logger.debug("Response Headers:");
        for (String name : response.getHeaderNames()) {
            logger.debug("\t" + name + ": " + response.getHeader(name));
        }
    }

    @Override
    public void printRequestBody(HttpServletRequest request) {
        try {
            CachedBodyHttpServletRequest cachedRequest = (CachedBodyHttpServletRequest) request
                    .getAttribute("cachedRequest");
            if (cachedRequest != null) {
                byte[] body = cachedRequest.getCachedBody();
                if (body != null && body.length > 0) {
                    String contentType = request.getContentType();
                    logger.debug("Request Body (" + contentType + "):");
                    logger.debug(formatBody(body, contentType));
                }
            }
        } catch (Exception e) {
            logger.debug("Error printing request body: " + e.getMessage());
        }
    }

    @Override
    public void printResponseBody(byte[] responseBody) {
        try {
            if (responseBody != null && responseBody.length > 0) {
                String contentType = "application/json"; // Default or get from response
                logger.debug("Response Body (" + contentType + "):");
                logger.debug(formatBody(responseBody, contentType));
            }
        } catch (Exception e) {
            logger.debug("Error printing response body: " + e.getMessage());
        }
    }

    private String formatBody(byte[] body, String contentType) {
        String content = new String(body, StandardCharsets.UTF_8);

        if (contentType != null) {
            if (contentType.contains("application/json")) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object json = mapper.readValue(content, Object.class);
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                } catch (Exception e) {
                }
            }
        }

        return content;
    }
}
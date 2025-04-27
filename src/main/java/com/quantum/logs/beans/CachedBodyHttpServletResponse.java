package com.quantum.logs.beans;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream cachedBody;
    private final HttpServletResponse response;
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
        this.cachedBody = new ByteArrayOutputStream();
        this.response = response;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response");
        }
        if (outputStream == null) {
            outputStream = new CachedBodyServletOutputStream(this.cachedBody, this.response.getOutputStream());
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response");
        }
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(this.cachedBody, this.response.getCharacterEncoding()));
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
        super.flushBuffer();
    }

    public byte[] getCachedBody() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
        return cachedBody.toByteArray();
    }

    private static class CachedBodyServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream cachedBody;
        private final ServletOutputStream outputStream;

        public CachedBodyServletOutputStream(ByteArrayOutputStream cachedBody, ServletOutputStream outputStream) {
            this.cachedBody = cachedBody;
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            cachedBody.write(b);
            outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public boolean isReady() {
            return outputStream.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            outputStream.setWriteListener(writeListener);
        }
    }
}
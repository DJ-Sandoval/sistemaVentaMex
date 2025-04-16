package com.VentaMex.apiVentaMex.util.components;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

@Component
public class RateLimitFilter implements Filter {
    private static final int REQUEST_LIMIT = 200; // Limite de solicitudes
    private static final long TIME_WINDOW = 120 * 1000; // 60 segundos

    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ip = httpRequest.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        requestMap.putIfAbsent(ip, new RequestInfo(0, currentTime));

        RequestInfo requestInfo = requestMap.get(ip);

        synchronized (requestInfo) {
            if (currentTime - requestInfo.startTime > TIME_WINDOW) {
                requestInfo.startTime = currentTime;
                requestInfo.requestCount = 0;
            }

        }
        if (requestInfo.requestCount < REQUEST_LIMIT) {
            requestInfo.requestCount++;
            chain.doFilter(request, response); // Continuar con la petición
        } else {
            httpResponse.setStatus(429); // HTTP 429 Too Many Requests
            httpResponse.getWriter().write("Demasiadas solicitudes, intenta más tarde.");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

    private static class RequestInfo {
        int requestCount;
        long startTime;

        public RequestInfo(int requestCount, long startTime) {
            this.requestCount = requestCount;
            this.startTime = startTime;
        }
    }
}

package com.sneakysquid.authenticator.filter;

import com.sneakysquid.authenticator.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EnhancedHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpServletRequest wrappedRequest = getWrappedRequest(httpRequest);
        chain.doFilter(wrappedRequest, response);
    }

    private static HttpServletRequest getWrappedRequest(HttpServletRequest httpRequest) {
        return new HttpServletRequestWrapper(httpRequest) {
            private final Map<String, String> customHeaders = new HashMap<>();

            final String token = httpRequest.getHeader("Authorization");
            final String username = JwtUtil.extractUsername(token);
            final List<String> authorities = JwtUtil.extractAuthorities(token);

            {
                customHeaders.put("X-User-Username", username);
                customHeaders.put("X-User-Authorities", String.join(",", authorities));
            }

            @Override
            public String getHeader(String name) {
                if (customHeaders.containsKey(name)) {
                    return customHeaders.get(name);
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (customHeaders.containsKey(name)) {
                    return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                Set<String> headerNames = new HashSet<>(Collections.list(super.getHeaderNames()));
                headerNames.addAll(customHeaders.keySet());
                return Collections.enumeration(headerNames);
            }
        };
    }
}

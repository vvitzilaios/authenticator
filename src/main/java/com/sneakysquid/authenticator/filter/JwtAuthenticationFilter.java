package com.sneakysquid.authenticator.filter;

import com.sneakysquid.authenticator.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Order(1)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwtToken = authenticationHeader.replace("Bearer ", "");
        final String username = JwtUtil.extractUsername(jwtToken);
        final List<String> authorities = JwtUtil.extractAuthorities(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (JwtUtil.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        HttpServletRequest wrappedRequest = getWrappedRequest(request, username, authorities);

        filterChain.doFilter(wrappedRequest, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") && path.contains("/login");
    }

    private static HttpServletRequest getWrappedRequest(HttpServletRequest httpRequest, String username, List<String> authorities) {
        return new HttpServletRequestWrapper(httpRequest) {
            private final Map<String, String> customHeaders = new HashMap<>();

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
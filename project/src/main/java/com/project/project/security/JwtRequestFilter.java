package com.project.project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional.ofNullable(request.getHeader("Authorization"))
                    .filter(requestTokenHeader -> requestTokenHeader.startsWith("Bearer "))
                    .map(requestTokenHeader -> requestTokenHeader.substring(7))
                    .filter(token -> !jwtTokenUtil.isTokenExpired(token))
                    .map(jwtTokenUtil::getUsernameFromToken)
                    .map(jwtUserDetailsService::loadUserByUsername)
                    .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                    .ifPresent(handleAuthToken(request));
        }
        filterChain.doFilter(request, response);
    }

    private Consumer<UsernamePasswordAuthenticationToken> handleAuthToken(HttpServletRequest request) {
        return authToken -> {
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        };
    }

}

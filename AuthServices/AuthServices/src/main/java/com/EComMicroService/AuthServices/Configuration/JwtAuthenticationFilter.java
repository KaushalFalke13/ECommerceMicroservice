package com.EComMicroService.AuthServices.Configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.EComMicroService.AuthServices.Service.JwtToken;
import com.EComMicroService.AuthServices.Service.UsersService;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtToken jwtUtil;
    private final UsersService userDetailsService;

    public JwtAuthenticationFilter(JwtToken jwtUtil,UsersService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response, 
                                     FilterChain filterChain) throws IOException, ServletException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        try {
            if (jwtUtil.validateToken(token)) {

                String username = jwtUtil.extractUsername(token);

                if (username != null &&  SecurityContextHolder.getContext().getAuthentication() == null) {

                    var userDetails = userDetailsService.loadUserByUsername(username);

                    if (username.equals(userDetails.getUsername())) {

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (JwtException ex) {
            System.out.println("Invalid JWT: " + ex.getMessage());
        }   
    }
    
    filterChain.doFilter(request, response);
}

}

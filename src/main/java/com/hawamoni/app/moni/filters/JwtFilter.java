package com.hawamoni.app.moni.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawamoni.app.moni.configurations.MyUserDetailsService;
import com.hawamoni.app.moni.response.ErrorResponse;
import com.hawamoni.app.moni.service.JwtService;
import com.hawamoni.app.moni.tokens.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ErrorResponse errorResponse;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;
        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                email = jwtService.extractEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtService.getTokenType(token).equals(TokenType.ACCESS.name())) {
                        UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
                        if (userDetails != null && jwtService.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                            log.info("user authenticated...");
                        }
                    }
                }
            }
            filterChain.doFilter(request,response);
        }
        catch(Exception e) {
            log.info("an error occurred!!: {}",e.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(
                    errorResponse.configureError(request,e)
            ));
            //throw new RuntimeException(e.getMessage());
        }
    }
}

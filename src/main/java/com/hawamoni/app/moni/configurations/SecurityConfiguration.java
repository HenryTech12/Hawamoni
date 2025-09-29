package com.hawamoni.app.moni.configurations;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.filters.AuthFilter;
import com.hawamoni.app.moni.filters.JwtFilter;
import com.hawamoni.app.moni.response.ErrorResponse;
import com.hawamoni.app.moni.service.JwtService;
import com.hawamoni.app.moni.service.UserService;
import com.hawamoni.app.moni.tokens.AccessToken;
import com.hawamoni.app.moni.tokens.JwtToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private String[] publicUrls = {
            "/moni/auth/**",
            "/moni/oauth/users",
            "/moni/token/refresh",
            "/moni/create",
            "/v3/api-docs/**",    // OpenAPI JSON
            "/swagger-ui.html",   // Swagger UI HTML entrypoint
            "/swagger-ui/**",     // Swagger UI resources (JS, CSS)
            "/webjars/**",        // (optional, legacy)
            "/actuator/**",
    };
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtFilter jwtFilter;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthFilter authFilter(AuthenticationManager authenticationManager) {
        AuthFilter authFilter = new AuthFilter();
        authFilter.setFilterProcessesUrl("/moni/auth/login");
        authFilter.setAuthenticationManager(authenticationManager);
        authFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {

            response.setStatus(HttpServletResponse.SC_OK);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
             if(!Objects.isNull(userPrincipal)){
                 UserDTO userDTO = userService.getUserByEmail(userPrincipal.getUsername());
                 AccessToken accessToken = jwtService.generateAccessKey(userDTO);
                 RefreshToken refreshToken = jwtService.generateRefreshToken(userDTO);

                 JwtToken jwtToken = new
                         JwtToken(refreshToken.getRefresh_token(),refreshToken.getRefresh_expiry_time(),
                         accessToken.getAccess_token(),accessToken.getAccess_expiry_time());
                 response.getWriter().write(objectMapper.writeValueAsString(jwtToken));
             }
        }));

        authFilter.setAuthenticationFailureHandler(((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ErrorResponse errorResponse =
                    new ErrorResponse();
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse.configureError(request,exception)));
        }));

        return authFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://127.0.0.1:5500","http://hawamoni.vercel.app"));
        corsConfiguration.setAllowedMethods(List.of("POST","GET","PUT","DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));


        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
                new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager)  throws  Exception {
        httpSecurity.csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(publicUrls)
                                .permitAll().anyRequest().authenticated())
                .addFilterAt(authFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}

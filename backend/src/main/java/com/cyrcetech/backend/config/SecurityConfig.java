package com.cyrcetech.backend.config;

import com.cyrcetech.backend.security.JwtAuthenticationFilter;
import com.cyrcetech.backend.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for Spring Security 7.
 * AuthenticationProvider is auto-configured by Spring Security
 * based on UserDetailsService and PasswordEncoder beans.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;
        private final UserDetailsService userDetailsService;

        public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                        UserDetailsService userDetailsService) {
                this.jwtTokenProvider = jwtTokenProvider;
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
                return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(org.springframework.security.config.Customizer.withDefaults())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html")
                                                .permitAll()
                                                // Public Endpoints (if any, e.g. status check)

                                                // User/Tech/Admin - Customers (User needs to register clients)
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/customers/**")
                                                .hasAnyRole("USER", "TECHNICIAN", "ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/customers/**")
                                                .hasAnyRole("USER", "TECHNICIAN", "ADMIN")
                                                // User/Tech/Admin - Equipment (User needs to register equipment)
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/equipment/**")
                                                .hasAnyRole("USER", "TECHNICIAN", "ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/equipment/**")
                                                .hasAnyRole("USER", "TECHNICIAN", "ADMIN")
                                                // Tech/Admin only - Spare parts
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/spare-parts/**")
                                                .hasAnyRole("TECHNICIAN", "ADMIN")

                                                // Tickets: Create (All), View All/Manage (Tech/Admin)
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/tickets")
                                                .authenticated()
                                                .requestMatchers("/api/tickets/**").hasAnyRole("TECHNICIAN", "ADMIN")

                                                // Invoices: Tech/Admin
                                                .requestMatchers("/api/invoices/**").hasAnyRole("TECHNICIAN", "ADMIN")

                                                // Admin Only
                                                .requestMatchers("/api/users/**").hasRole("ADMIN")
                                                .requestMatchers("/api/reports/**").hasRole("ADMIN")

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}

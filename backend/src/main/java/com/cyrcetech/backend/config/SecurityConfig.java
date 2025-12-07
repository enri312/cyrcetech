package com.cyrcetech.backend.config;

import com.cyrcetech.backend.security.JwtAuthenticationFilter;
import com.cyrcetech.backend.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationProvider authenticationProvider;
        private final UserDetailsService userDetailsService;

        public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                        AuthenticationProvider authenticationProvider,
                        UserDetailsService userDetailsService) {
                this.jwtTokenProvider = jwtTokenProvider;
                this.authenticationProvider = authenticationProvider;
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

                                                // User/Tech/Admin
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/customers/**")
                                                .hasAnyRole("TECHNICIAN", "ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/equipment/**")
                                                .hasAnyRole("TECHNICIAN", "ADMIN")
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
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}

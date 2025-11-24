package com.alerom.rng.archive.rng_archive_server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the application.
 * It sets up the security filter chain, defines authentication providers,
 * and configures access rules for different endpoints.
 *
 * @author Alejo Romero
 * @version 1.0
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    /**
     * Constructs the SecurityConfiguration with necessary dependencies.
     *
     * @param jwtUtil The utility class for JWT operations.
     * @param jwtUserDetailsService The service to load user details.
     */
    public SecurityConfiguration(JwtUtil jwtUtil, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    /**
     * Creates and configures the JWT request filter.
     *
     * @return The configured JwtRequestFilter.
     */
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, jwtUserDetailsService);
    }

    /**
     * Provides the AuthenticationManager bean, which is responsible for
     * authenticating incoming requests.
     *
     * @param authenticationConfiguration The configuration for authentication.
     * @return The configured AuthenticationManager.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides the PasswordEncoder bean for hashing and verifying passwords.
     *
     * @return The BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the security filter chain for the application.
     * It configures CSRF protection, session management, authorization rules,
     * and adds the custom JWT filter.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/api/summon-simulator/**").permitAll()

                        .requestMatchers("/api/user/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/user/update/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/user/update/password/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/userArtifact/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/userArtifact").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/userArtifact/update/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/userArtifact/delete/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/pull/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/pull").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/pull/update/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/pull/delete/{id}").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/api/admin/characterBanner/create").hasRole("ADMIN")
                        .requestMatchers("/api/admin/characterBanner").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/characterBanner/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/characterBanner/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/weaponBanner/create").hasRole("ADMIN")
                        .requestMatchers("/api/admin/weaponBanner").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/weaponBanner/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/weaponBanner/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/news/create").hasRole("ADMIN")
                        .requestMatchers("/api/admin/news").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/news/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/news/delete7{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/artifactSet/create").hasRole("ADMIN")
                        .requestMatchers("/api/admin/artifactSet").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/artifactSet/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/artifactSet/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/unit/create").hasRole("ADMIN")
                        .requestMatchers("/api/admin/unit").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/unit/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/unit/delete/{id}").hasRole("ADMIN")

                        .requestMatchers("/api/statistics/artifact").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/statistics/pull").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
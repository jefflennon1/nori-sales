package com.noriservices.norisales.shared.security;

import com.noriservices.norisales.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Value("${application.cors.allowed-origin}")
    private String allowedOrigin;

     private final JwtAuthenticationFilter jwtAuthenticationFilter;

     public SecurityConfigurations(JwtAuthenticationFilter jwtAuthenticationFilter){
         this.jwtAuthenticationFilter = jwtAuthenticationFilter;
     };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/payments/webhook").permitAll()
                                .requestMatchers(HttpMethod.GET, "/orders/my-orders").hasRole(UserRole.BUYER.getRole())
                                .requestMatchers(HttpMethod.GET,  "/orders/**").hasAnyRole(UserRole.BUYER.getRole(), UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.GET, "/orders").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.POST, "/orders").hasRole(UserRole.BUYER.getRole())
                                .requestMatchers(HttpMethod.POST, "/products").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT, "/products/update").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.POST, "/categories").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.PUT, "/categories/update").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole(UserRole.ADMIN.getRole())
                                .requestMatchers(HttpMethod.POST, "/payments/*/pix").hasRole(UserRole.BUYER.getRole())
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(allowedOrigin));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

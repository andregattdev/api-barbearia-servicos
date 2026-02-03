package com.app.api_servicos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> {
                
                // 1. ROTAS PÚBLICAS
                req.requestMatchers(HttpMethod.POST, "/api/login").permitAll();
                req.requestMatchers(HttpMethod.POST, "/api/usuarios/registrar").permitAll();
                
                // 2. SERVIÇOS: Clientes podem ver, mas só Admin cria/edita
                req.requestMatchers(HttpMethod.GET, "/api/servicos").permitAll();
                req.requestMatchers("/api/servicos/**").hasAuthority("ADMIN");

                // 3. CLIENTES (Gestão de Prontuários): Apenas Admin acessa a lista completa
                req.requestMatchers("/api/clientes/**").hasAuthority("ADMIN");

                // 4. FATURAMENTO: Restrito ao Admin
                req.requestMatchers("/api/agendamentos/faturamento/**").hasAuthority("ADMIN");

                // 5. AGENDAMENTOS: 
                // Qualquer um logado (Admin ou Cliente) pode criar um agendamento
                req.requestMatchers(HttpMethod.POST, "/api/agendamentos").authenticated();
                // Admin pode listar todos ou gerenciar status
                req.requestMatchers(HttpMethod.GET, "/api/agendamentos").hasAuthority("ADMIN");
                req.requestMatchers(HttpMethod.PATCH, "/api/agendamentos/**").hasAuthority("ADMIN");
                req.requestMatchers(HttpMethod.DELETE, "/api/agendamentos/**").hasAnyAuthority("ADMIN", "CLIENTE");

                // 6. PROTEÇÃO GLOBAL
                req.anyRequest().authenticated();
            })
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Aqui definimos EXPLICITAMENTE a origem para não dar erro com allowCredentials
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
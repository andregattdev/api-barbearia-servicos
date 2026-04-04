package com.app.api_servicos.config;

import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Value("${api.security.token.secret:minha-chave-secreta-muito-forte-de-pelo-menos-32-caracteres}")
    private String secret;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            try {
                Key key = Keys.hmacShaKeyFor(secret.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String login = claims.getSubject();
                String perfil = claims.get("perfil", String.class);

                Usuario usuario = usuarioRepository.findByLogin(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usuario,
                                null,
                                List.of(new SimpleGrantedAuthority(perfil))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

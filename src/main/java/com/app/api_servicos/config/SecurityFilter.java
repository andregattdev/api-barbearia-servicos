package com.app.api_servicos.config;

<<<<<<< HEAD
import com.app.api_servicos.model.Usuario;
import com.app.api_servicos.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
=======
import com.app.api_servicos.repository.UsuarioRepository;
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
=======
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
<<<<<<< HEAD
import java.security.Key;
import java.util.List;
=======
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc

@Component
public class SecurityFilter extends OncePerRequestFilter {

<<<<<<< HEAD
    @Value("${api.security.token.secret:minha-chave-secreta-muito-forte-de-pelo-menos-32-caracteres}")
    private String secret;

    @Autowired
    private UsuarioRepository usuarioRepository; // injeta o repositório

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

                // Busca o usuário no banco
                Usuario usuario = usuarioRepository.findByLogin(login)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                // Cria a autenticação com o objeto Usuario como principal
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
=======
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String token = recuperarToken(request);
        
        if (token != null) {
            String login = tokenService.validarToken(token);
            if (login != null) {
                repository.findByLogin(login).ifPresent(usuario -> {
                    // Aqui "autenticamos" o usuário no contexto do Spring
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, null); 
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
>>>>>>> 36d150d0b02a6471b12188008fd8062c1d1d37bc

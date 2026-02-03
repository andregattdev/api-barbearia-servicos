package com.app.api_servicos.config;

import com.app.api_servicos.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    // Essa chave deve ser mantida em segredo no seu application.properties
    @Value("${api.security.token.secret:minha-chave-secreta-muito-forte-de-pelo-menos-32-caracteres}")
    private String secret;

    private static final long EXPIRATION_TIME = 86400000; // 1 dia em milisegundos

    public String gerarToken(Usuario usuario) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setIssuer("API Servicos")
                .setSubject(usuario.getLogin())
                .claim("perfil", usuario.getPerfil().name()) // Guardamos o perfil no token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validarToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null; // Token inválido ou expirado
        }
    }
}
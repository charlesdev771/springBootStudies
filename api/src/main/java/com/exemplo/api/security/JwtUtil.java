package com.exemplo.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long expirationMillis;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMillis) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("A chave JWT deve ter no mínimo 32 caracteres");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    /** Gera token JWT para o email */
    public String gerarToken(String email) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    /** Decodifica payload sem parserBuilder */
    public JwtPayload decodificarToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> map = objectMapper.readValue(payloadJson, Map.class);

            JwtPayload payload = new JwtPayload();
            payload.setEmail((String) map.get("sub"));
            payload.setExp(((Number) map.get("exp")).longValue() * 1000); // converte para millis
            return payload;
        } catch (Exception e) {
            return null;
        }
    }

    /** Retorna true se token válido e não expirado */
    public boolean validarToken(String token) {
        JwtPayload payload = decodificarToken(token);
        return payload != null && payload.getExp() > System.currentTimeMillis();
    }

    /** Retorna email do token ou null se inválido */
    public String obterEmailDoToken(String token) {
        JwtPayload payload = decodificarToken(token);
        if (payload == null) return null;
        return payload.getEmail();
    }

    // DTO do payload
    public static class JwtPayload {
        private String email;
        private long exp;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public long getExp() { return exp; }
        public void setExp(long exp) { this.exp = exp; }
    }
}

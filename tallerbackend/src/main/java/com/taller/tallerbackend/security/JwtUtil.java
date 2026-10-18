package com.taller.tallerbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {


    private final SecretKey key = Keys.hmacShaKeyFor(
            "eaXpStjYeINnyAjDi5w0K3nNveyVjlT58eum3g7vaIk".getBytes()
    );

    private final long EXPIRACION_MS = 1000 * 60 * 60 * 8; // 8 horas

    public String generarToken(UUID usuarioId, String nombre, String rol) {
        return Jwts.builder()
                .subject(usuarioId.toString())
                .claim("nombre", nombre)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRACION_MS))
                .signWith(key)
                .compact();
    }

    public Claims validarYExtraer(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
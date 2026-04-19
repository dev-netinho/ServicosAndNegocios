package com.deefy.group2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secretKey; // A chave mestre usada para assinar e validar os tokens

    @Value("${api.security.token.expiration}")
    private Long jwtExpiration; // O tempo de vida (TTL) do token em milissegundos

    // Gera o token para o usuário
    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    // Construtor do Token
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Define o e-mail como identificador do token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Data de validade
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina o token usando o algoritmo HS256
                .compact(); // Finaliza e gera a String final do JWT
    }

    // Valida se o token pertence ao usuário e não expirou
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Extrai o 'username' (e-mail) do interior do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Verifica se a data atual já passou da data de expiração do token
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Recupera a data de validade (claim 'exp') gravada no interior do token para controle de tempo de vida
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método genérico para extrair qualquer informação específica (Claim) do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // "Abre" o token usando a chave secreta e retorna todo o corpo (Claims) dele
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Decodifica a chave Base64 e a transforma em uma chave HMAC-SHA segura
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

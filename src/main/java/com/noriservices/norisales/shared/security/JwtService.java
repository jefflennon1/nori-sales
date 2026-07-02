package com.noriservices.norisales.shared.security;

import com.noriservices.norisales.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private long expiration;

    private static final String ISSUER = "nori-sales-api";

    public String generateToken(User user){
       return Jwts.builder()
               .subject(user.getUsername())
               .issuer(ISSUER)
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + expiration))
               .signWith(getSigningKey())
               .claim("role", user.getRole().name())
               .compact();
    }

    public boolean isTokenValid(String token, UserDetails user){
        try {
            final String username = extractClaim(token, Claims::getSubject);

            return username.equals(user.getUsername())
                    && !isTokenExpired(token)
                    && user.isEnabled();
        }catch (JwtException e){
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

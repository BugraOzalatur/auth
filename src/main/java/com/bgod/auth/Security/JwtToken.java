package com.bgod.auth.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtToken {
    @Value("${value.jwt.secretKey}")
    private  String secretKey;
    @Value("${value.jwt.expiration}")
    private int expiration;


    public String generateJwtToken(
            UserDetails user
    ){
        return Jwts
                .builder()
                .setHeaderParam("type","JWT")
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails user){
        final String id = extractUserId(token);
        boolean isTokenValid = Objects.equals(id, user.getUsername()) && !isTokenExpired(token);

        return isTokenValid;
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserId(String token){

        return extractClaim(token, Claims::getSubject);
    }







//kullanıcı profilini alması için böyle bir şey yazdım eğer tokenı bulursa id yi döndürüyor
    public UUID getSignedProfile(@NonNull HttpServletRequest request ){
        String authHeader=request.getHeader("Authorization");
        if(authHeader != null){
            String token=authHeader.substring(7);
            String headerId = extractUserId(token);

            return UUID.fromString(headerId);
        }
        return null;
    }



    private Date extractExpiration(String token){
        return extractClaim(token,Claims :: getExpiration);
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

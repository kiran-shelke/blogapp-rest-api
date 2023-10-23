package com.springboot.blog.config;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-millis}")
    private long jwtExpirationDate;

    //Validate jwt token
    public boolean validate(String token)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }
        catch (MalformedJwtException ex)
        {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Invalid token");
        }
        catch (ExpiredJwtException ex)
        {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Expired token");
        }
        catch (UnsupportedJwtException ex)
        {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"UnSupported token");
        }
        catch (IllegalArgumentException ex)
        {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Empty token strin");
        }

    }
    //get username from jwt token
    public String getUsername(String token)
    {
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    //Generate JWT token
    public String generateToken(Authentication authentication)
    {
        String username= authentication.getName();
        Date currentDate=new Date();
        Date expDate= new Date(currentDate.getTime()+jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(key())
                .compact();
    }
    private Key key()
    {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
}

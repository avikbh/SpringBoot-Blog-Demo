/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.blog.util;

import com.app.blog.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 *
 * @author 1460344
 */
@Component
public class JWTUtils {

    public String CreateJWTToken(Users user) {
        
        Claims claims= Jwts.claims();
        claims.put("name", user.getUserName());
        claims.put("email", user.getEmail());
        claims.put("user_id", user.getUserId());
        claims.setSubject("MY Blog");
        claims.setIssuedAt(new Date());
        
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET)
                .compact();
        
        return token;
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getEmailFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);

        return claims.get("email").toString();
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //validate token without expiry
    public Boolean validateTokenWithoutExpiry(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()));
    }

    //retrieve userid from jwt token
    public String getUserId(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get("user_id").toString();
    }

    //retrieve jwt token from Auth Header
    public String getTokenFromHeader(String authorization) {
        String token="";
        if(null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        return token;
    }



}

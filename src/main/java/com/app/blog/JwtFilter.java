/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.blog;

import com.app.blog.util.Constants;
import com.app.blog.util.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author 1460344
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
//    @Override
//    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    @Autowired
    private JWTUtils jwtUtility;

    @Autowired
    private CustomUserDetailService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, HttpServerErrorException {
        String authorization = httpServletRequest.getHeader("authorization");
        String token = null;
        String userName = null;

        if(null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);

//            try {

                userName = jwtUtility.getEmailFromToken(token);
//            }
//            catch (Exception exc) {
//                HttpServletResponse res= new HttpServletResponse();
//                res.setStatus(500);
////		res.setContentType("Unable to read JSON value");
//                res.getWriter().write("Unable to read JSON value");
//            }
        }

        if(null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails
                    = userService.loadUserByUsername(userName);

            if(jwtUtility.validateTokenWithoutExpiry(token,userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    
    
}

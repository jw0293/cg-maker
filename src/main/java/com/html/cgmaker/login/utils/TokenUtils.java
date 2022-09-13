package com.html.cgmaker.login.utils;

import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.domain.enums.UserRole;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequiredArgsConstructor
@Log4j2
@Component
@PropertySource("classpath:application-oauth.yml")
public class TokenUtils {

    private final CookieUtils cookieUtils;

    private static String secretKey;

    // Access 토큰 유효시간 15분
    static final long AccessTokenValidTime = 15 * 60 * 1000L;
    // Refresh Token 유효시간 2시간
    static final long RefreshTokenValidTime = 120 * 60 * 1000L;

    public long getRefreshTokenValidTime(){
        return RefreshTokenValidTime;
    }

    @Value("${password}")
    public void setSecretKey(String path){
        secretKey = path;
    }

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token generateToken(String uid, String role){

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + AccessTokenValidTime))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + RefreshTokenValidTime))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact()
        );
    }

    public boolean isValidToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e){
            return false;
        }
    }


    public String getUid(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Cookie reissueAccessToken(HttpServletResponse response, String refreshToken) {
        String email = getUid(refreshToken);
        Token newToken = generateToken(email, UserRole.USER.getKey());

        Cookie cookie = cookieUtils.createCookie(AuthConstants.AUTH_HEADER, newToken.getAccessToken());

        return cookie;
    }

}
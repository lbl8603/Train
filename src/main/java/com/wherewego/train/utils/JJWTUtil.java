package com.wherewego.train.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletException;
import java.util.Date;

/**
 * @Author:lubeilin
 * @Date:Created in 10:19 2020/2/10
 * @Modified By:
 */
public class JJWTUtil {
    private final static String ACCESS_TOKEN_KEY = "******************";//access_token私钥
    private final static String REFRESH_TOKEN_KEY = "****************";//refresh_token私钥
    private final static long ACCESS_TOKEN_EXP = 1000L * 60 * 60 * 24;//过期时间
    private final static long REFRESH_TOKEN_EXP=1000L*60*60*24*30;
    public static String getRefreshToken(String userCode) {

        return Jwts.builder()
                .setSubject(userCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_KEY)
                .compact();
    }
    public static Claims getRefreshTokenClaims(String refreshToken) throws ServletException {
        try {
            Claims claims = Jwts.parser().setSigningKey(REFRESH_TOKEN_KEY).parseClaimsJws(refreshToken).getBody();
            return claims;
        } catch (ExpiredJwtException e1) {
            throw new ServletException("refresh token expired");
        } catch (Exception e) {
            throw new ServletException("other token exception");
        }
    }

    public static String getAccessToken(String userCode) {

        return Jwts.builder()
                .setSubject(userCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_KEY)
                .compact();
    }
    /**
     * @Desc:只要不正确就会抛出异常
     **/
    public static Claims getAccessTokenClaims(String token) throws ServletException {
        try {
            Claims claims = Jwts.parser().setSigningKey(ACCESS_TOKEN_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e1) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("other token exception");
        }
    }
}

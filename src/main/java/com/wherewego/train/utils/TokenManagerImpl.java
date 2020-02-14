package com.wherewego.train.utils;

import com.wherewego.train.exception.WhereWeGoException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;

import static com.wherewego.train.constant.ErrorCode.TokenError;

/**
 *
 * @Author:lubeilin
 * @Date:Created in 10:49 2020/2/10
 * @Modified By:
 */
@Component
public class TokenManagerImpl implements TokenManger{

    @Override
    public String createAccessToken(String sub) {
        return JJWTUtil.getAccessToken(sub);
    }

    @Override
    public String createRefreshToken(String sub) {
        return JJWTUtil.getRefreshToken(sub);
    }

    @Override
    public String getAccessTokenSub(String accessToken) {
        try {
            Claims claims = JJWTUtil.getAccessTokenClaims(accessToken);
            return claims.getSubject();
        } catch (ServletException e) {
            throw new WhereWeGoException(TokenError.getCode(),TokenError.getMsg());
        }
    }

    @Override
    public String getRefreshTokenSub(String refreshToken) {
        try {
            Claims claims = JJWTUtil.getRefreshTokenClaims(refreshToken);
            return claims.getSubject();
        } catch (ServletException e) {
            throw new WhereWeGoException(TokenError.getCode(),TokenError.getMsg());
        }
    }
}

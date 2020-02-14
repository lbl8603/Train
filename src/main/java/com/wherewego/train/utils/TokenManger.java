package com.wherewego.train.utils;

/**
 * 用户令牌生成
 * @Author:lubeilin
 * @Date:Created in 10:42 2020/2/10
 * @Modified By:
 */
public interface TokenManger {
    String createAccessToken(String sub);
    String createRefreshToken(String sub);
    String getAccessTokenSub(String accessToken);
    String getRefreshTokenSub(String refreshToken);

}

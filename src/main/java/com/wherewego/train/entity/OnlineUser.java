package com.wherewego.train.entity;

/**
 * 登录用户信息类
 * @Author:lubeilin
 * @Date:Created in 17:30 2020/2/9
 * @Modified By:
 */
public class OnlineUser {
    private String userCode;
    private String token;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

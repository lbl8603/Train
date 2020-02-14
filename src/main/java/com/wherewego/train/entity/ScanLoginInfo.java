package com.wherewego.train.entity;

/**
 * 扫码登录授权信息实体
 * @Author:lubeilin
 * @Date:Created in 13:30 2020/2/13
 * @Modified By:
 */
public class ScanLoginInfo {
    private String code;
    private long expire;
    private String sign;

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

package com.wherewego.train.service;

import com.wherewego.train.entity.ScanLoginInfo;

/**
 * 扫码登录公共处理逻辑
 * @Author:lubeilin
 * @Date:Created in 13:27 2020/2/13
 * @Modified By:
 */
public interface ScanLoginService {
    byte[] loginQRCode(String code,long expire,String sign) throws Exception;
    ScanLoginInfo getCode();
    void scanCodeSuccess(String code,long expire,String sign);
}

package com.wherewego.train.utils;

import org.springframework.util.DigestUtils;

/**
 * @Author:lubeilin
 * @Date:Created in 11:38 2020/2/12
 * @Modified By:
 */
public class MD5Util {
    //盐，用于混淆md5
    private static final String slat = "***************************";
    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}

package com.wherewego.train.service;

import com.wherewego.train.entity.OnlineUser;
import com.wherewego.train.entity.UserInfo;

/**
 * 用户信息管理、登录、注销
 * @Author:lubeilin
 * @Date:Created in 10:45 2020/2/9
 * @Modified By:
 */
public interface UserService {
    OnlineUser login(String code);
    OnlineUser logout(String userCode);
    boolean update(UserInfo userInfo);
    void ackLogin(String code,long expire,String sign);
}

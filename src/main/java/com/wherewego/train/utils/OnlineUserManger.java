package com.wherewego.train.utils;

import com.wherewego.train.entity.OnlineUser;

/**
 * 当前请求的用户信息管理类
 * @Author:lubeilin
 * @Date:Created in 18:32 2020/2/9
 * @Modified By:
 */
public class OnlineUserManger {
    private static final ThreadLocal<OnlineUser> threadLocal = new ThreadLocal<>();
    public static OnlineUser getUser(){
        return threadLocal.get();
    }
    public static void setUser(OnlineUser onlineUser){
        threadLocal.set(onlineUser);
    }
    public static void remove(){
        threadLocal.remove();
    }
}

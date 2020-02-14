package com.wherewego.train.Interceptor;

import com.wherewego.train.constant.ErrorCode;
import com.wherewego.train.entity.OnlineUser;
import com.wherewego.train.exception.WhereWeGoException;
import com.wherewego.train.utils.OnlineUserManger;
import com.wherewego.train.utils.TokenManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截，用户校验用户信息，并将当前请求的信息暂存在ThreadLocal中
 * @Author:lubeilin
 * @Date:Created in 20:17 2020/2/9
 * @Modified By:
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenManger tokenManager;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String str = request.getHeader("Authorization");
       // System.out.println(str);
        if(str==null||str.equals("")){
            throw new WhereWeGoException(ErrorCode.ClientError.getCode(),"缺少Authorization");
        }
        int e = str.indexOf(' ');
        if(e<=0||e==str.length()-1||!str.startsWith("token")){
            throw new WhereWeGoException(ErrorCode.ClientError.getCode(),"Authorization参数错误");
        }
        String token = str.substring(e+1);
        String usercode = tokenManager.getAccessTokenSub(token);
        OnlineUser user = new OnlineUser();
        user.setUserCode(usercode);
        user.setToken(token);
        OnlineUserManger.setUser(user);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable
    Exception ex) throws Exception {
        OnlineUserManger.remove();
    }
}

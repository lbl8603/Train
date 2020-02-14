package com.wherewego.train.controller;

import com.wherewego.train.entity.ResponseData;
import com.wherewego.train.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息api
 * @Author:lubeilin
 * @Date:Created in 22:35 2020/2/8
 * @Modified By:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信授权登录
     * @param code 微信下发的授权码
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ResponseData login(String code){
//        JsonConfig jsonConfig = new JsonConfig();
//        jsonConfig.registerJsonValueProcessor(Timestamp.class,new TimestampJsonValueProcessor());
        return new ResponseData( userService.login(code));
    }

    /**
     * 扫码确认授权网页端登录
     * @param code 唯一授权码
     * @param expire 有效期至
     * @param sign 数据签名
     * @return
     */
    @RequestMapping(value = "/ackLogin",method = RequestMethod.GET)
    public ResponseData ackLogin(String code,long  expire,String sign){
        userService.ackLogin(code,expire,sign);
        return new ResponseData("");
    }
}

package com.wherewego.train.service.impl;

import com.wherewego.train.constant.ErrorCode;
import com.wherewego.train.dao.UserInfoRepository;
import com.wherewego.train.entity.MsgVO;
import com.wherewego.train.entity.OnlineUser;
import com.wherewego.train.entity.UserInfo;
import com.wherewego.train.exception.WhereWeGoException;
import com.wherewego.train.service.UserService;
import com.wherewego.train.utils.OnlineUserManger;
import com.wherewego.train.utils.TokenManger;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.jms.Topic;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author:lubeilin
 * @Date:Created in 16:05 2020/2/9
 * @Modified By:
 */
@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private TokenManger tokenManager;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;

    @Override
    public OnlineUser login(String code) {
        if(code==null||code.equals("")){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),ErrorCode.ParamError.getMsg());
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";
        Map<String, Object> params = new HashMap<>();
        params.put("js_code", code);
        params.put("appid","");
        params.put("secret","");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String > responseEntity = restTemplate.getForEntity(url, String.class, params);
        String rs = responseEntity.getBody();
        logger.debug(rs);
        JSONObject jsonObject = JSONObject.fromObject(rs);
        String openid;
        try {
            openid = jsonObject.getString("openid");
            //String session_key = jsonObject.getString("session_key");用作解密用户信息
        }catch (Exception e){
            throw new WhereWeGoException(ErrorCode.CodeError.getCode(),ErrorCode.CodeError.getMsg());
        }
        UserInfo userInfo = repository.getUserInfoByWxOpenid(openid);
        if(userInfo==null||userInfo.getUserCode()==null){//第一次登录
            userInfo = new UserInfo();
            userInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userInfo.setModifyTime(userInfo.getCreateTime());
            userInfo.setUserCode(UUID.randomUUID().toString().replace("-",""));//生成随机值
            userInfo.setWxOpenid(openid);
            repository.save(userInfo);
        }
        String token = tokenManager.createAccessToken(userInfo.getUserCode());
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setToken(token);
        onlineUser.setUserCode(userInfo.getUserCode());
        return onlineUser;
    }

    @Override
    public OnlineUser logout(String userCode) {
        return null;
    }

    @Override
    public boolean update(UserInfo userInfo) {
        return false;
    }

    /**
     * 确认登录不校验expire和sign，因为微信小程序参数长度限制，只能携带code
     * @param code
     * @param expire
     * @param sign
     */
    @Override
    public void ackLogin(String code,long expire, String sign) {
//        if(MD5Util.getMD5(code+"_"+expire).equals(sign)){
            String userCode = OnlineUserManger.getUser().getUserCode();
            MsgVO msgVO = new MsgVO();
            msgVO.setCode(code);
            msgVO.setAccessToken(tokenManager.createAccessToken(userCode));
            msgVO.setRefreshToken(tokenManager.createRefreshToken(userCode));
            msgVO.setUserCode(userCode);
            jmsMessagingTemplate.convertAndSend(topic, JSONObject.fromObject(msgVO).toString());
//        }else{
//            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),"参数错误");
//        }
    }

}

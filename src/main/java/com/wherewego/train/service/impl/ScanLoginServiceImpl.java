package com.wherewego.train.service.impl;

import com.wherewego.train.entity.MsgVO;
import com.wherewego.train.entity.ScanLoginInfo;
import com.wherewego.train.service.ScanLoginService;
import com.wherewego.train.utils.MD5Util;
import com.wherewego.train.utils.QRCodeUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Topic;
import java.util.UUID;

/**
 * @Author:lubeilin
 * @Date:Created in 13:31 2020/2/13
 * @Modified By:
 */
@Service
public class ScanLoginServiceImpl implements ScanLoginService {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;
    @Override
    public byte[] loginQRCode(String code,long expire,String sign) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://wherewego.top/html/ack.html");
        stringBuilder.append("?code=").append(code).append("&expire=").append(expire)
                .append("&sign=").append(sign);
        byte[] bytes = QRCodeUtils.createQRCodeBytes(stringBuilder.toString(),200,200,"JPG");
        return bytes;
    }

    /**
     * 生成的code不能超过32位
     * @return
     */
    @Override
    public ScanLoginInfo getCode() {
        ScanLoginInfo scanLoginInfo = new ScanLoginInfo();
        scanLoginInfo.setCode(UUID.randomUUID().toString().replace("-",""));
        scanLoginInfo.setExpire(System.currentTimeMillis()/1000+10*60);
        scanLoginInfo.setSign(MD5Util.getMD5(scanLoginInfo.getCode()+"_"+scanLoginInfo.getExpire()));
        return scanLoginInfo;
    }

    /**
     * 扫码确认不校验expire和sign，因为微信小程序参数长度限制，只能携带code
     * @param code
     * @param expire
     * @param sign
     */
    @Override
    public void scanCodeSuccess(String code, long expire, String sign) {
        MsgVO msgVO = new MsgVO();
        msgVO.setCode(code);
        msgVO.setAccessToken("scan success");
        jmsMessagingTemplate.convertAndSend(topic, JSONObject.fromObject(msgVO).toString());
    }
}

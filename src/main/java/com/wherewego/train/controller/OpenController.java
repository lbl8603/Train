package com.wherewego.train.controller;

import com.wherewego.train.entity.ResponseData;
import com.wherewego.train.service.ScanLoginService;
import com.wherewego.train.service.TrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Topic;

/**
 * 公共api，该目录下不校验身份信息
 * @Author:lubeilin
 * @Date:Created in 15:49 2020/2/12
 * @Modified By:
 */
@RestController
@RequestMapping("/open")
public class OpenController {
    @Resource
    private TrainTicketService ticketService;
    @Resource
    private ScanLoginService scanLoginService;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;

    /**
     * 扫车票上的二维码获取车票信息
     * @param code
     * @param sign
     * @return
     */
    @RequestMapping(value = "/scanTrainTicketQRCode",method = RequestMethod.GET)
    public ResponseData scanQRCode(String code, String sign){
        return new ResponseData(ticketService.scanQRCode(code,sign));
    }

    @RequestMapping(value = "/loginQRCodeLoad",method = RequestMethod.GET)
    public byte[] loginQRCodeLoad(String code,long expire,String sign) throws Exception {
        return scanLoginService.loginQRCode(code,expire,sign);
    }
    @RequestMapping(value = "/getLoginCode",method = RequestMethod.GET)
    public ResponseData getLoginCode() throws Exception {
        return new ResponseData(scanLoginService.getCode());
    }
    @RequestMapping(value = "/scanCodeSuccess",method = RequestMethod.GET)
    public ResponseData scanCodeSuccess(String code,long expire,String sign) throws Exception {
        scanLoginService.scanCodeSuccess(code, expire, sign);
        return new ResponseData("");
    }
}

package com.wherewego.train.controller;

import com.wherewego.train.constant.ErrorCode;
import com.wherewego.train.entity.ResponseData;
import com.wherewego.train.entity.TrainTicketInfo;
import com.wherewego.train.exception.WhereWeGoException;
import com.wherewego.train.service.TrainTicketService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 火车票信息api
 * @Author:lubeilin
 * @Date:Created in 10:46 2020/2/8
 * @Modified By:
 */
@RestController
@RequestMapping("/train")
public class TrainTicketController {
    private static Logger logger = LoggerFactory.getLogger(TrainTicketController.class);
    @Resource
    private TrainTicketService service;

    /**
     * 使用图片色值数组识别火车票信息
     * @param data
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ocrTrainTicketImgFrame",method = RequestMethod.POST)
    public ResponseData ocrTrainTicketImgFrame(@RequestBody JSONObject data) throws IOException {
//        logger.debug(data.toString());
        TrainTicketInfo ticketInfo = service.ocrTrainTicketImgFrame((Integer[])data.getJSONArray("arr").toArray(new Integer[0]),
                data.getInt("width"),data.getInt("height"));
        return new ResponseData(ticketInfo);
    }

    /**
     * 上传图片文件识别火车票信息
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ocrTrainTicket",method = RequestMethod.POST)
    public ResponseData ocrTrainTicket(@RequestParam("file") MultipartFile file) throws Exception{
        logger.debug(file.getOriginalFilename()+file.getSize());
        BASE64Encoder base64Encoder =new BASE64Encoder();
        String base64EncoderImg =base64Encoder.encode(file.getBytes());
        TrainTicketInfo ticketInfo = service.ocrTrainTicket(base64EncoderImg);
        return new ResponseData(ticketInfo);
    }

    /**
     * 生成火车票二维码
     * @param trainTicketCode
     * @param content
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createTrainTicketQRCode",method = RequestMethod.GET)
    public ResponseData createTrainTicketQRCode(String trainTicketCode,String content) throws Exception{
        String path = service.createTrainTicketQRCodeImage(trainTicketCode,content);
        return new ResponseData(path);
    }

    /**
     * 保存火车票信息
     * @param data
     * @return
     */
    @RequestMapping(value = "/saveTrainTicket",method = RequestMethod.POST)
    public ResponseData saveTrainTicket(@RequestBody JSONObject data){
        try {
            TrainTicketInfo ticketInfo = (TrainTicketInfo)JSONObject.toBean(data,TrainTicketInfo.class);
            return new ResponseData(service.saveTrainTicket(ticketInfo));
        }catch (Exception e){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),ErrorCode.ParamError.getMsg());
        }
    }

    /**
     * 分页查找火车票
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/findTrainTicketList",method = RequestMethod.GET)
    public ResponseData findTrainTicketList(int page,int limit){
        return new ResponseData(service.findList(page,limit));
    }

    /**
     * 根据火车票编码查找
     * @param ticketCode
     * @return
     */
    @RequestMapping(value = "/deleteTrainTicket",method = RequestMethod.GET)
    public ResponseData deleteTrainTicket(String ticketCode){
        return new ResponseData(service.deleteTrainTicket(ticketCode));
    }
   }

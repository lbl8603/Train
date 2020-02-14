package com.wherewego.train.service;

import com.wherewego.train.entity.TrainTicketInfo;
import org.springframework.data.domain.Page;

/**
 * 火车票数据处理逻辑
 * @Author:lubeilin
 * @Date:Created in 19:16 2020/2/7
 * @Modified By:
 */
public interface TrainTicketService {
    String createTrainTicketQRCodeImage(String ticketCode,String content);
    TrainTicketInfo ocrTrainTicket(String imageBase64);
    TrainTicketInfo ocrTrainTicketImgFrame(Integer[] array,int width,int height);
    TrainTicketInfo saveTrainTicket(TrainTicketInfo ticketInfo);
    int deleteTrainTicket(String ticketCode);
    Page<TrainTicketInfo> findList(int page,int size);
    TrainTicketInfo scanQRCode(String code,String sign);
}

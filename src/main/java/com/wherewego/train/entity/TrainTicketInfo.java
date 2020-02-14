package com.wherewego.train.entity;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 火车票信息
 * @Author:lubeilin
 * @Date:Created in 17:08 2020/2/7
 * @Modified By:
 */
@Entity
@Table(name="train_ticket")
public class TrainTicketInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trainTicketID;
    private String ticketCode;
    private String userCode;
    private String ticketNum;
    private String startStation;
    private String destinationStation;
    private String date;
    private String trainNum;
    private String seat;
    private String name;
    private String price;
    private String seatCategory;
    private String ID;
    private String qrCodePath;//二维码路径
    private Timestamp createTime;
    private Timestamp modifyTime;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getTrainTicketID() {
        return trainTicketID;
    }

    public void setTrainTicketID(Integer trainTicketID) {
        this.trainTicketID = trainTicketID;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeatCategory() {
        return seatCategory;
    }

    public void setSeatCategory(String seatCategory) {
        this.seatCategory = seatCategory;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }
}

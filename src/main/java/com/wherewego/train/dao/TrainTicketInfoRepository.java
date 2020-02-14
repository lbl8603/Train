package com.wherewego.train.dao;

import com.wherewego.train.entity.TrainTicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author:lubeilin
 * @Date:Created in 12:03 2020/2/10
 * @Modified By:
 */
@Repository
public interface TrainTicketInfoRepository extends JpaRepository<TrainTicketInfo,Integer>, JpaSpecificationExecutor<TrainTicketInfo> {
    TrainTicketInfo getTrainTicketInfoByTicketCode(String ticketCode);
    int deleteTrainTicketInfoByTicketCodeAndUserCode(String ticketCode,String userCode);
}

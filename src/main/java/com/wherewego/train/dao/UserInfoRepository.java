package com.wherewego.train.dao;

import com.wherewego.train.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author:lubeilin
 * @Date:Created in 15:51 2020/2/9
 * @Modified By:
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
    UserInfo getUserInfoByWxOpenid(String wxOpenid);
}

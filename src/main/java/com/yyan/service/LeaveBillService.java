package com.yyan.service;

import com.yyan.pojo.LeaveBill;

import java.util.Map;

public interface LeaveBillService {

    // 添加
    void insertLeaveBill(LeaveBill leaveBill);

    // 查询
    Map<String, Object> selectListLeaveBill(Map map); // 查询区块

    // 更新
    void updateLeaveBill(LeaveBill leaveBill);

    // 删除
    void deleteLeaveBill(String id);


}

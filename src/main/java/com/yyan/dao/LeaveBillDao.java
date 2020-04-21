package com.yyan.dao;

import com.yyan.pojo.LeaveBill;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LeaveBillDao {

    // 添加
    void insertLeaveBill(LeaveBill leaveBill);

    // 查询
    List<Map> selectListLeaveBill(Map map); // 查询区块
    Integer countListLeaveBill(Map map); // 总条数

    // 更新
    void updateLeaveBill(LeaveBill leaveBill);

    // 删除
    void deleteLeaveBill(String id);

}

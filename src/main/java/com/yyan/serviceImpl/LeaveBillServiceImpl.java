package com.yyan.serviceImpl;

import com.yyan.dao.LeaveBillDao;
import com.yyan.pojo.LeaveBill;
import com.yyan.service.LeaveBillService;
import com.yyan.utils.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LeaveBillServiceImpl extends BaseServiceImpl implements LeaveBillService {


    @Autowired
    private LeaveBillDao leaveBillDao;


    /**
     * 插入请假单
     *
     * @param leaveBill
     */
    @Override
    public void insertLeaveBill(LeaveBill leaveBill) {

        leaveBillDao.insertLeaveBill(leaveBill);
    }

    /**
     * 查询请假单
     *
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> selectListLeaveBill(Map map) {

        List<Map> newList = this.leaveBillDao.selectListLeaveBill(checkPageSize(map));

        Integer count = this.leaveBillDao.countListLeaveBill(map);
        return this.queryListSuccess(newList, count, map); //查询成功

    }

    @Override
    public void updateLeaveBill(LeaveBill leaveBill) {

    }

    @Override
    public void deleteLeaveBill(String id) {

    }
}

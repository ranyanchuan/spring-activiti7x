package com.yyan.controller;

import com.yyan.pojo.LeaveBill;
import com.yyan.serviceImpl.LeaveBillServiceImpl;
import com.yyan.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
@RequestMapping("/api/leaveBill")
public class LeaveBillController extends BaseController {

    @Autowired
    private LeaveBillServiceImpl leaveBillService;

    /**
     * 添加请假单
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Map<String, Object> insertUser(@RequestBody LeaveBill leaveBill) {

        try {
            this.leaveBillService.insertLeaveBill(leaveBill);
            return this.buildSuccess();
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }


    /**
     * 根据条件查询
     */
    @RequestMapping("/select")
    @ResponseBody
    public Map<String, Object> getBlock(@RequestBody Map<String,Object> map) {
        try {
            return this.buildSuccess(this.leaveBillService.selectListLeaveBill(map));
        } catch (Exception exp) {
            return this.buildError(exp.getMessage());
        }
    }

    /**
     * 添加请假单
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> insertUser(@RequestParam String id) {

        try {
            this.leaveBillService.deleteLeaveBill(id);
            return this.buildSuccess();
        } catch (Exception exp) {
            System.out.println(exp);
            return this.buildError(exp.getMessage());
        }
    }


}

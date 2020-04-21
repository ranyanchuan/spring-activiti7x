package com.yyan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class LeaveBill {

    private String id;
    private String title;
    private String content;
    private String days;
    private String state;// 0未提交,1审批中，2审批完成，3已放弃
    private Date leaveDate;
    private Date createTime;
    private Date updateTime;
    private String userId;


}

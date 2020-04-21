package com.yyan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class LeaveBill {

    private String id;
    private String title;
    private String content;
    private String days;
    private String state;
    private Date leaveDate;
    private Date createTime;
    private Date updateTime;
    private String userId;


}

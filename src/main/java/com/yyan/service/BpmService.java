package com.yyan.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BpmService {


    void deployment(); // 流程部署

    Map<String, Object> selectListProcessDeployment(Map map); // 查看流程部署信息

    String selectProcessImg(String deploymentId) throws IOException; // 查看流程流程图

    Map<String, Object> selectListProcessDefinition(Map map); // 查看流程定义信息


    void deleteProcessDeployment(String deploymentId); // 根据流程部署id删除流程部署信息

    void startProcess(Map<String, String> map); // 启动流程

    Map<String, Object> selectListSelfTask(Map map); // 查看自己任务

    List<Map> selectListCommentByTaskId(Map<String,Object> map); // 通过任务id 查询评论

    void finishTask(Map<String,String> map); // 完成自己的任务



}

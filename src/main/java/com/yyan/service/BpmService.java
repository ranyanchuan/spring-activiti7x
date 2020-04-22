package com.yyan.service;


import java.io.IOException;
import java.util.Map;

public interface BpmService {


    void deployment(); // 流程部署

    Map<String, Object> selectListProcessDeployment(Map map); // 查看流程部署信息

    String selectProcessImg(String deploymentId) throws IOException; // 查看流程流程图

    Map<String, Object> selectListProcessDefinition(Map map); // 查看流程定义信息


    void deleteProcessDeployment(String deploymentId); // 根据流程部署id删除流程部署信息

    void startProcess(Map<String, String> map); // 启动流程

    Map<String, Object> selectListSelfTask(Map map); // 查看自己任务


}

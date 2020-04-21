package com.yyan.service;


import java.util.Map;

public interface BpmService {


    void deployment(); // 流程部署

    Map<String, Object> selectListProcessDeployment(Map map); // 查看流程部署信息

    Map<String, Object> selectListProcessDefinition(Map map); // 查看流程定义信息


    void deleteProcessDeployment(String deploymentId); // 根据流程部署id删除流程部署信息

}

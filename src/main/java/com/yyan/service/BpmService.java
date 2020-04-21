package com.yyan.service;


import org.activiti.engine.repository.ProcessDefinition;

import java.util.List;
import java.util.Map;

public interface BpmService {


    void deployment(); // 流程部署

    Map<String, Object> selectListProcessDefinition(Map map); // 查看流程定义信息

}

package com.yyan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BpmService {

    void selectListProcessDefinition(List list); // 查看流程定义信息


}

package com.yyan.serviceImpl;

import com.yyan.service.BpmService;
import com.yyan.utils.BaseServiceImpl;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BpmServiceImpl extends BaseServiceImpl implements BpmService {


    // 获取流程引擎
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 流程部署
     */
    @Override
    public void deployment() {

        //2.得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 3.进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram.bpmn")  //添加bpmn资源
                .addClasspathResource("diagram.svg")
                .name("请假申请单流程")  // 流程部署名称
                .deploy();

        //4.输出部署的一些信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());
    }

    /**
     * 获取流程部署信息
     *
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> selectListProcessDeployment(Map param) {
        Map newParam = checkPageSize(param);
        Integer pageIndex = (Integer) newParam.get("pageIndex");
        Integer size = (Integer) newParam.get("size");

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Integer count = Math.toIntExact(repositoryService.createDeploymentQuery().count());

        List<Deployment> list = repositoryService.createDeploymentQuery().listPage(pageIndex * size, (pageIndex + 1) * size);

        // 数据处理
        List<Map> newList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Deployment deployment = list.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deployment.getId());
            map.put("name", deployment.getName());
            map.put("deploymentTime", deployment.getDeploymentTime());
            newList.add(map);
        }
        return this.queryListSuccess(newList, count, param); //查询成功;
    }


    // 查看流程部署信息


    // 查看流程定义信息
    @Override
    public Map<String, Object> selectListProcessDefinition(Map param) {


        Map newParam = checkPageSize(param);
        Integer pageIndex = (Integer) newParam.get("pageIndex");
        Integer size = (Integer) newParam.get("size");

        RepositoryService repositoryService = processEngine.getRepositoryService();

//        Integer count = this.blockDao.countListBlock(map);
        Integer count = Math.toIntExact(repositoryService.createProcessDefinitionQuery().count());

        //获取查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery
                .orderByProcessDefinitionVersion().desc().listPage(pageIndex * size, (pageIndex + 1) * size);

        List<Map> newList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ProcessDefinition processDefinition = list.get(i);
            System.out.println("流程定义id：" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义key：" + processDefinition.getKey());
            System.out.println("流程定义版本：" + processDefinition.getVersion());
            System.out.println("流程部署id：" + processDefinition.getDeploymentId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", processDefinition.getId());
            map.put("name", processDefinition.getName());
            map.put("key", processDefinition.getKey());
            map.put("version", processDefinition.getVersion());
            map.put("deploymentId", processDefinition.getDeploymentId());
            newList.add(map);
        }
        System.out.println(newList);
        System.out.println(count);

        return this.queryListSuccess(newList, count, param); //查询成功
    }
}

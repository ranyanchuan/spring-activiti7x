package com.yyan.serviceImpl;

import com.yyan.pojo.LeaveBill;
import com.yyan.service.BpmService;
import com.yyan.utils.BaseServiceImpl;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class BpmServiceImpl extends BaseServiceImpl implements BpmService {


    // 获取流程引擎
    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    //2.得到RepositoryService实例
    private RepositoryService repositoryService = processEngine.getRepositoryService();


    /**
     * 流程部署
     */
    @Override
    public void deployment() {
        // 3.进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram.bpmn")  //添加bpmn资源
                .addClasspathResource("diagram.svg")
                .name("请假申请单流程001")  // 流程部署名称
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

        // 获取流程部署总算
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

    /**
     * 查看流程图
     *
     * @param deploymentId
     * @return
     */
    @Override
    public String selectProcessImg(String deploymentId) throws IOException {

        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        // 获取图片名称
        String resourceName = processDefinition.getDiagramResourceName();
        System.out.println("resourceName");
        System.out.println(resourceName);

        // 使用部署id和图片名称查询图片流
        InputStream stream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        byte[] bytes;

        bytes = new byte[stream.available()];
        stream.read(bytes);
        return new String(bytes);
    }


    // 查看流程定义信息
    @Override
    public Map<String, Object> selectListProcessDefinition(Map param) {

        Map newParam = checkPageSize(param);
        Integer pageIndex = (Integer) newParam.get("pageIndex");
        Integer size = (Integer) newParam.get("size");

        // 获取流程定义总算
        Integer count = Math.toIntExact(repositoryService.createProcessDefinitionQuery().count());

        //获取查询器
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().desc().listPage(pageIndex * size, (pageIndex + 1) * size);

        List<Map> newList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ProcessDefinition processDefinition = list.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", processDefinition.getId()); // 流程定义id
            map.put("name", processDefinition.getName());// 流程定义名称
            map.put("key", processDefinition.getKey());// 流程定义key
            map.put("version", processDefinition.getVersion());// 流程定义版本
            map.put("deploymentId", processDefinition.getDeploymentId());// 流程部署id
            newList.add(map);
        }
        return this.queryListSuccess(newList, count, param); //查询成功
    }

    // 根据流程部署id删除流程部署信息
    @Override
    public void deleteProcessDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public void startProcess(Map<String, String> map) {

        String processDefinitionKey = map.get("processDefinitionKey"); // 流程key
        String formId = map.get("formId"); // 表单id
        // 生成 业务键
        String businessKey = processDefinitionKey+":"+formId; //

        // 设置下一任务审批人
        Map<String,Object> variable=new HashMap<>();
        variable.put("username","laoda");

        RuntimeService runtimeService = processEngine.getRuntimeService(); // 根据流程定义key启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variable);

        // todo 更新请假单信息



    }


}

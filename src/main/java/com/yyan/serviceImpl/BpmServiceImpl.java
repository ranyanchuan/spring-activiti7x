package com.yyan.serviceImpl;

import com.yyan.pojo.LeaveBill;
import com.yyan.service.BpmService;
import com.yyan.utils.BaseServiceImpl;
import org.activiti.engine.*;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.BeanUtils;
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
    // 任务服务
    private TaskService taskService = processEngine.getTaskService();

    RuntimeService runtimeService = processEngine.getRuntimeService(); // 根据流程定义key启动流程


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
        String businessKey = processDefinitionKey + ":" + formId; //

        // 设置下一任务审批人
        Map<String, Object> variable = new HashMap<>();

        // todo 从token 中获取
        variable.put("username", "laoda");

        processEngine.getRuntimeService(); // 根据流程定义key启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variable);

        // todo 更新请假单信息


    }

    @Override
    public Map<String, Object> selectListSelfTask(Map param) {

        Map newParam = checkPageSize(param);
        Integer pageIndex = (Integer) newParam.get("pageIndex");
        Integer size = (Integer) newParam.get("size");

        // todo 从token 中获取
        // 设置办理人
        String assignee = "laoda";
        // 查询总数
        Integer count = Math.toIntExact(taskService.createTaskQuery().taskAssignee(assignee).count());

        List<Task> list = this.taskService.createTaskQuery().taskAssignee(assignee).listPage(pageIndex * size, (pageIndex + 1) * size);

        List<Map> newList = new ArrayList<>();
        for (Task task : list) {

            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId()); // 任务ID
            map.put("name", task.getName());// 任务名称
            map.put("createTime", task.getCreateTime());// 任务的创建时间
            map.put("assignee", task.getAssignee());// 任务的办理人
            map.put("processInstanceId", task.getProcessInstanceId());// 流程实例ID
            map.put("executionId", task.getExecutionId());// 执行对象ID
            map.put("processDefinitionId", task.getProcessDefinitionId());// 流程定义ID
            newList.add(map);
        }

        // todo 根据任务ID查询表单新
        // todo 根据任务ID查询连线信息
        // todo 根据任务ID查询任务批准信息 act_hi_comment
        return this.queryListSuccess(newList, count, param); //查询成功


    }

    @Override
    public List<Map> selectListCommentByTaskId(Map<String,Object> param) {

        String taskId= (String) param.get("taskId");

        // 1,根据任务ID查询任务实例
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2,从任务里面取出流程实例ID
        // todo 优化
        String processInstanceId = task.getProcessInstanceId();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);

        List<Map> newList = new ArrayList<>();
        if (null != comments && comments.size() > 0) {
            for (Comment comment : comments) {

                Map<String, Object> map = new HashMap<>();
                map.put("id", comment.getId()); // 评论id
                map.put("message", comment.getFullMessage()); // 评论信息
                map.put("taskId", comment.getTaskId()); // 任务ID
                map.put("processInstanceId", comment.getProcessInstanceId()); // 流程ID
                map.put("userId", comment.getUserId()); // 用户ID
                newList.add(map);

            }
        }

        return newList;
    }

    @Override
    public void finishTask(Map<String, String> map) {


        String taskId =  map.get("taskId");// 任务ID
        String outcome =  map.get("outcome");// 连接名称
//        String leaveId = map.get("leaveId");// 请假单ID
        String comment =map.get("comment");// 批注信息

        // 1,根据任务ID查询任务实例
        Task task =taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2,从任务里面取出流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        // 设置批注人名
        // todo token
        String userName = "laoda";
        /*
         * 因为批注人是org.activiti.engine.impl.cmd.AddCommentCmd 80代码使用 String userId =
         * Authentication.getAuthenticatedUserId(); CommentEntity comment = new
         * CommentEntity(); comment.setUserId(userId);
         * Authentication这类里面使用了一个ThreadLocal的线程局部变量
         */

        // 添加批注信息
        this.taskService.addComment(taskId, processInstanceId, "[" + outcome + "]" + comment);
        // 完成任务
        Map<String, Object> variables = new HashMap<>();
        variables.put("outcome", outcome);
        this.taskService.complete(taskId, variables);
        // 判断任务是否结束
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (null == processInstance) {
//            LeaveBill leaveBill = new LeaveBill();
//            leaveBill.setId(leaveBillId);
            // 说明流程结束
//            if (outcome.equals("放弃")) {
//                leaveBill.setState(SYSConstast.STATE_LEAVEBILL_THREE);// 已放弃
//            } else {
//                leaveBill.setState(SYSConstast.STATE_LEAVEBILL_TOW);// 审批完成
//            }
//            this.billMapper.updateByPrimaryKeySelective(leaveBill);
        }




    }


}

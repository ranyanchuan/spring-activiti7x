import com.yyan.App;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {App.class})
public class ProcessDefinitionTest {


    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 初始化流程表
     */
    @Test
    public void initProcessTable() {
        //创建ProcessEngineConfiguration  也就是创建 25 张表
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml"); //通过ProcessEngineConfiguration创建ProcessEngine，此时会创建数据库
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }


    /**
     * 部署流程定义
     */
    @Test
    public void deployProcess() {


        //2.得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.进行部署
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("holiday.bpmn")  //添加bpmn资源
//                .addClasspathResource("holiday.png")
//                .name("请假申请单流程")
//                .deploy();

        // bpmn 测试
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("diagram.bpmn")  //添加bpmn资源
                .addClasspathResource("diagram.svg")
                .name("请假申请单流程22")
                .deploy();


        //4.输出部署的一些信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());

    }


    /**
     * 启动流程实例
     */

    // 启动一个流程实例
    @Test
    public void startProcessInstance() {
        // 获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService(); // 根据流程定义key启动流程
        ProcessInstance processInstance = runtimeService
//                .startProcessInstanceByKey("holiday");
                .startProcessInstanceByKey("Process_1");
        System.out.println("流程定义id : " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id:" + processInstance.getId());
        System.out.println(" 当前活动Id: " +
                processInstance.getActivityId());
    }


    /**
     * 查询流程
     */
    @Test
    public void findPersonalTask() {

        TaskService taskService = processEngine.getTaskService();
        //获取查询对象
        List<Task> list = taskService.createTaskQuery()//创建任务查询对象
                .list();
        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
            }
        }

//        //1.得到ProcessEngine对象
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//
//        //2.创建RepositoryService对象
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//
//        //3.得到ProcessDefinitionQuery对象,可以认为它就是一个查询器
//        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//
//        //4.设置条件，并查询出当前的所有流程定义   查询条件：流程定义的key=holiday
//        //orderByProcessDefinitionVersion() 设置排序方式,根据流程定义的版本号进行排序
//        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey("test-designer")
//                .orderByProcessDefinitionVersion()
//                .desc().list();
//
//        //5.输出流程定义信息
//        for (ProcessDefinition processDefinition : list) {
//            System.out.println("流程定义ID：" + processDefinition.getId());
//            System.out.println("流程定义名称：" + processDefinition.getName());
//            System.out.println("流程定义的Key：" + processDefinition.getKey());
//            System.out.println("流程定义的版本号：" + processDefinition.getVersion());
//            System.out.println("流程部署的ID:" + processDefinition.getDeploymentId());
//
//        }

    }


    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask() {
        //获取taskService
        TaskService taskService = processEngine.getTaskService();
        //获取查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //
        TaskQuery myProcess = taskQuery.processDefinitionKey("test-designer");
        //获取当前之前任务代理人
        TaskQuery taskAssignee = myProcess.taskAssignee("laoda");
        //查询当前代理人要执行的任务
        List<Task> list = taskAssignee.list();
        for (int i=0;i<list.size();i++){
            Task task = list.get(i);
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务执行人："+task.getAssignee());
        }

    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        //任务ID
        String taskId = "2505";
        processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .complete(taskId);
        System.out.println("完成任务：任务ID：" + taskId);
    }

    //可以分配个人任务从一个人到另一个人（认领任务）
    @Test
    public void setAssigneeTask() {
        //任务ID
        String taskId = "5804";
        //指定的办理人
        String userId = "张翠山";
        processEngine.getTaskService()//
                .setAssignee(taskId, userId);
    }


}

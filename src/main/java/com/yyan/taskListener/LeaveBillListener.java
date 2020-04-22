package com.yyan.taskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class LeaveBillListener implements TaskListener {

    /**用来指定任务的办理人*/
    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("dddddd");
        // todo 获取指定通过当前人 查询上一级信息
        //指定个人任务的办理人，也可以指定组任务的办理人
        //个人任务：通过类去查询数据库，将下一个任务的办理人查询获取，然后通过setAssignee()的方法指定任务的办理人
        delegateTask.setAssignee("laoer");
    }
}

package com.bobo.flow;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class FlowableSpringBoot28ApplicationTests03 {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;
    @Autowired
    private FormRepositoryService formRepositoryService;

    /**
     * 部署流程
     */
    @Test
    void testDeploy() throws Exception {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("任务回退01.bpmn20.xml")
                .name("任务回退01")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
        System.out.println("部署开始的时间：" + new Date());
        //TimeUnit.MINUTES.sleep(3);
    }

    /**
     * 启动流程实例
     */
    @Test
    void startProcess(){
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("myProcess:1:4");
        System.out.println("processInstance.getId() = " + processInstance.getId());
    }

    /**
     * 完成任务
     */
    @Test
    void completeTask(){
        Task task = taskService.createTaskQuery()
                .processDefinitionId("myProcess:1:4")
                .taskAssignee("user3")
                .singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 回退操作
     */
    @Test
    void rollbackTask(){
        // 当前的Task对应的用户任务的Id
        List<String> currentActivityIds = new ArrayList<>();
        currentActivityIds.add("usertask4");
        // 需要回退的目标节点的用户任务Id
        String newActivityId = "usertask1";
        // 回退操作
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("2501")
                .moveActivityIdsToSingleActivityId(currentActivityIds,newActivityId)
                .changeState();
    }

}

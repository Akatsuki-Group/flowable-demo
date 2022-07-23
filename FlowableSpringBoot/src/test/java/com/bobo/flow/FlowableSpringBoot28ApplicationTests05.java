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
class FlowableSpringBoot28ApplicationTests05 {

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
     * moveActivityIdTo 可以从当前的流程跳转到任意的节点
     */
    @Test
    void backProcess(){
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("2501")
                .moveActivityIdTo("usertask4","usertask3")
                .changeState();
    }

    @Test
    void backProcess1(){
        List<String> newActivityIds = new ArrayList<>();
        newActivityIds.add("usertask1");
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("2501")
                //.moveActivityIdTo("usertask4","usertask1")
                .moveSingleActivityIdToActivityIds("usertask4",newActivityIds)
                .changeState();

    }

    @Test
    void backProcess2(){
        List<String> newActivityIds = new ArrayList<>();
        newActivityIds.add("usertask1");
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("2501")
                //.moveActivityIdTo("usertask4","usertask1")
                //.moveSingleActivityIdToActivityIds("usertask4",newActivityIds)
                .moveExecutionToActivityId("17501","usertask3")
                .changeState();

    }
}

package com.bobo.flow.controller;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class FlowableController {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/deploy")
    public String deploy(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/等待定时器启动事件.bpmn20.xml")
                .name("等待定时器启动事件")
                .deploy();
        System.out.println(deploy.getId()+":" + new Date());
        return "部署流程成功:"+deploy.getId();
    }
}

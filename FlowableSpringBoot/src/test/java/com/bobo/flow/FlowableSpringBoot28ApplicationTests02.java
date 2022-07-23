package com.bobo.flow;

import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.impl.persistence.entity.HistoricFormPropertyEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FlowableSpringBoot28ApplicationTests02 {

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

    /**
     * Deploy
     */
    @Test
    void testDeploy() throws Exception {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/动态表单01.bpmn20.xml")
                .name("动态表单01")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
        System.out.println("部署开始的时间：" + new Date());
        //TimeUnit.MINUTES.sleep(3);
    }

    /**
     * 查看部署的流程内置的表单信息
     */
    @Test
    void getStartFormData(){
        //processEngine.getFormService().getStartFormData()
        StartFormData startFormData = formService.getStartFormData("myProcess:1:4");
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            System.out.println("formProperty.getId() = " + formProperty.getId());
            System.out.println("formProperty.getName() = " + formProperty.getName());
            System.out.println("formProperty.getValue() = " + formProperty.getValue());
        }
    }

    /**
     * 启动带表单的流程实例
     */
    @Test
    void startProcess1(){
        Map<String,Object> map = new HashMap<>();
        map.put("days",4);
        map.put("startTime","20220404");
        map.put("reason","世界很大，我想出去看看...");
        runtimeService.startProcessInstanceById("myProcess:1:4",map);

    }

    /**
     * 第二种启动方式
     */
    @Test
    void startProcess2(){
        Map<String,String> map = new HashMap<>();
        map.put("days","2");
        map.put("startTime","20220405");
        map.put("reason","想休息休息");
        // runtimeService.startProcessInstanceWithForm()
        formService.submitStartFormData("myProcess:1:4",map);

    }

    /**
     * 查看Task对应的表单数据
     */
    @Test
    void getTaskFormData(){
        TaskFormData taskFormData = formService.getTaskFormData("15003");
        /*Task task = taskFormData.getTask();
        taskService.setAssignee(task.getId(),"zhangsan");*/
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            System.out.println("formProperty.getId() = " + formProperty.getId());
            System.out.println("formProperty.getName() = " + formProperty.getName());
            System.out.println("formProperty.getValue() = " + formProperty.getValue());
        }
    }

    /**
     * 更新Form表单中的数据
     */
    @Test
    void updateTaskFormData(){
        Map<String,String> map = new HashMap<>();
        map.put("days","5");
        formService.saveFormData("2508",map);
    }


    /**
     * 完成任务
     */
    @Test
    void completeTask(){

        taskService.complete("2508");
    }

    @Test
    void getHistoryTaskFormData(){
        List<HistoricDetail> list = processEngine.getHistoryService()
                .createHistoricDetailQuery()
                .taskId("2508")
                .formProperties()
                .list();

        for (HistoricDetail historicDetail : list) {
            HistoricFormPropertyEntityImpl hf = (HistoricFormPropertyEntityImpl) historicDetail;
            System.out.println("hf.getPropertyId() = " + hf.getPropertyId());
            System.out.println("hf.getPropertyValue() = " + hf.getPropertyValue());
        }
    }





}

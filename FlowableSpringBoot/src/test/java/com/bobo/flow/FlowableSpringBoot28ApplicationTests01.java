package com.bobo.flow;

import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.impl.persistence.entity.HistoricFormPropertyEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormInfo;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.model.FormField;
import org.flowable.form.model.SimpleFormModel;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlowableSpringBoot28Application.class)
class FlowableSpringBoot28ApplicationTests01 {



    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private TaskService taskService;


    @Autowired
    private RuntimeService runtimeService;


    /**
     * Deploy
     */
    @Test
    void testDeploy() throws Exception {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/动态表单02.bpmn20.xml")
                .name("动态表单02")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
        System.out.println("部署开始的时间：" + new Date());
        //TimeUnit.MINUTES.sleep(3);
    }

    @Test
    public void getStartFromData(){
        String departemntId = "de851d85-b31a-11ec-a038-c03c59ad2248";
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(departemntId)
                .singleResult();
        StartFormData startFormData = processEngine.getFormService()
                .getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            String id = formProperty.getId();
            String name = formProperty.getName();
            FormType type = formProperty.getType();
            System.out.println("id = " + id);
            System.out.println("name = " + name);
            System.out.println("type.getClass() = " + type.getClass());
        }
    }


    /**
     * 正常的启动流程
     */
    @Test
    void startFlow() throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("days","5");
        map.put("startDate","20220403");
        map.put("reason","想休息下");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("myProcess:6:deb7c648-b31a-11ec-a038-c03c59ad2248",map);
    }


    /**
     * 通过FormService来启动一个表单流程
     * @throws Exception
     */
    @Test
    void startFormFlow() throws Exception{
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("4da14de4-b313-11ec-882d-c03c59ad2248")
                .singleResult();
        Map<String,String> map = new HashMap<>();
        map.put("days","2");
        map.put("startDate","20220406");
        map.put("reason","出去玩玩");
        ProcessInstance processInstance = processEngine.getFormService().submitStartFormData(processDefinition.getId(), map);

    }

    /**
     * 保存表单数据
     */
    @Test
    void saveFormData(){
        String taskId = "24170530-b31b-11ec-a870-c03c59ad2248";
        Map<String,String> map = new HashMap<>();
        map.put("days","3");
        map.put("startDate","20220407");
        map.put("reason","出去玩玩11");
        processEngine.getFormService().saveFormData(taskId,map);
    }

    /**
     * 保存表单数据并完成任务
     */
    @Test
    void submitTaskFormData(){
        String taskId = "24170530-b31b-11ec-a870-c03c59ad2248";
        Map<String,String> map = new HashMap<>();
        map.put("days","4");
        map.put("startDate","20220408");
        map.put("reason","出去玩玩");
        processEngine.getFormService().submitTaskFormData(taskId,map);
    }

    /**
     * 根据Task编号来查看表单数据
     */
    @Test
    void getTaskFormData(){
        String taskId = "24170530-b31b-11ec-a870-c03c59ad2248";
        TaskFormData taskFormData = processEngine.getFormService().getTaskFormData(taskId);
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            System.out.println("formProperty.getId() = " + formProperty.getId());
            System.out.println("formProperty.getName() = " + formProperty.getName());
            System.out.println("formProperty.getValue() = " + formProperty.getValue());
        }
    }

    /**
     * 查看已经完成的Task的表单数据
     */
    @Test
    void getHisTaskFormData(){
        String taskId = "80efeb32-b313-11ec-a7ff-c03c59ad2248";
        List<HistoricDetail> list = processEngine.getHistoryService()
                .createHistoricDetailQuery()
                .taskId(taskId)
                .formProperties()
                .list();
        for (HistoricDetail historicDetail : list) {
            HistoricFormPropertyEntityImpl his = (HistoricFormPropertyEntityImpl) historicDetail;
            System.out.println("his.getPropertyId() = " + his.getPropertyId());
            System.out.println("his.getPropertyValue() = " + his.getPropertyValue());
        }
    }

    /**
     * 通过信号发送来触发信号启动事件的执行
     * 全局的信息
     */
    @Test
    void signalGolbal() throws Exception {
        runtimeService.signalEventReceived("signal2");
        // 我们得保证容器的运行，所以需要阻塞
        TimeUnit.MINUTES.sleep(1);
    }

    /**
     * 发送ProcessInstance信号
     * @throws Exception
     */
    @Test
    void signalProcessInstance() throws Exception {
        runtimeService.signalEventReceived("secondSingal","dbdffde3-afd4-11ec-92f6-c03c59ad2248");
        // 我们得保证容器的运行，所以需要阻塞
        TimeUnit.MINUTES.sleep(1);
    }

    /**
     * 边界事件-发布消息
     */
    @Test
    void recevedMsg(){
        // 需要查询到executionId
        String processExecutionId = "1d516be5-af12-11ec-89a4-c03c59ad2248";
        // 我们需要根据流程实例编号找到对应的执行编号
       /* Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId("event008:1:9217aa5e-af0e-11ec-b11f-c03c59ad2248")
                .singleResult();
        System.out.println("----------->"+execution.getId());*/
        runtimeService.messageEventReceived("第三个消息",processExecutionId);
    }

    /**
     * complete Task
     */
    @Test
    void completeTask(){
        Task task = taskService.createTaskQuery()
                .processInstanceId("9f7ec730-b16d-11ec-9b9c-c03c59ad2248")
                .taskAssignee("zhangsan")
                .singleResult();
        if(task != null){
            taskService.complete(task.getId());
            System.out.println("complete ....");
        }
    }


    @Test
    void completeTask1(){
        Map<String,Object> map = new HashMap<>();
        map.put("flag",true); // 设置为true 结束多人会签
        taskService.complete("16775186-b232-11ec-863c-c03c59ad2248",map);
        System.out.println("complete ....");
    }

    /**
     * 删除流程定义
     */
    @Test
    void deleteProcess(){
        repositoryService.deleteDeployment("f4a91e15-aeff-11ec-9d3c-c03c59ad2248",true);
    }


    /*******************外部表单的处理***************************/

    /**
     * 部署流程：
     */
    @Test
    public void deploy(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/动态表单02.bpmn20.xml")
                .name("动态表单02")
                .deploy();
        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
        System.out.println("部署开始的时间：" + new Date());
    }

    @Autowired
    private FormRepositoryService formRepositoryService;

    /**
     * 部署form表单
     */
    @Test
    public void deployForm() throws Exception{

        FormDeployment formDeployment = formRepositoryService.createDeployment()
                .addClasspathResource("holiday.form")
                .name("test")
                .parentDeploymentId("1")
                .deploy();
        System.out.println("formDeployment.getId() = " + formDeployment.getId());
    }


    /**
     * 启动流程实例，并且设置对应的值
     */
    @Test
    void startTask(){
        Map<String,Object> map = new HashMap<>();
        map.put("days","4");
        map.put("startTime","20220404");
        map.put("reason","出去玩玩");
        ProcessInstance processInstance = runtimeService.startProcessInstanceWithForm(
                "myProcess:1:4"
                , null
                , map
                , "请假流程");
        String id = processInstance.getId();
        System.out.println("id = " + id);

    }

    /**
     * 查看任务的表单数据
     */
    @Test
    public void getTaskFormData1(){
        Task task = taskService.createTaskQuery()
                .processDefinitionId("myProcess:1:4")
                .taskAssignee("zhangsan")
                .singleResult();
        FormInfo formInfo = runtimeService.getStartFormModel("myProcess:1:4", "5001");
        System.out.println("formInfo.getId() = " + formInfo.getId());
        System.out.println("formInfo.getName() = " + formInfo.getName());
        System.out.println("formInfo.getKey() = " + formInfo.getKey());
        SimpleFormModel formModel = (SimpleFormModel) formInfo.getFormModel();
        List<FormField> fields = formModel.getFields();
        for (FormField field : fields) {
            System.out.println("field.getId() = " + field.getId());
            System.out.println("field.getName() = " + field.getName());
            System.out.println("field.getValue() = " + field.getValue());
        }
        System.out.println("formModel = " + formModel);
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTaskForm(){
        Map<String,Object> map = new HashMap<>();
        map.put("days","4");
        map.put("startTime","20220404");
        map.put("reason","出去玩玩");
        String taskId = "5010";
        String formDefinitionId = "2503";
        String outcome = "波哥";
        taskService.completeTaskWithForm(taskId,formDefinitionId,outcome,map);
    }




    @Autowired
    HistoryService historyService;


}

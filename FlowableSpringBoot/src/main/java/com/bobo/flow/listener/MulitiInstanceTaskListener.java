package com.bobo.flow.listener;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component("mulitiInstanceTaskListener")
public class MulitiInstanceTaskListener implements Serializable {

    /**
     *
     * @param execution
     */
    public void completeListener(DelegateExecution execution){
        System.out.println("任务："+execution.getId());
        System.out.println("persons:" + execution.getVariable("persons"));
        System.out.println("person:" + execution.getVariable("person"));
    }
}

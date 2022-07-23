package com.bobo.flow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.time.LocalDateTime;
import java.util.Date;

public class MyTwoJavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("MyTwoDelegate---->预订机票流程执行了" + LocalDateTime.now().toString());
    }
}

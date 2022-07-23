package com.bobo.flow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.time.LocalDateTime;
import java.util.Date;

public class MyThreeJavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("MyThreeDelegate---->机票预订取消了...." + LocalDateTime.now().toString());
    }
}

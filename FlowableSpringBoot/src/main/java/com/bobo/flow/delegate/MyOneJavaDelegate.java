package com.bobo.flow.delegate;

import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.time.LocalDateTime;
import java.util.Date;

public class MyOneJavaDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("微信支付-----》MyOneDelegate" + LocalDateTime.now().toString());
        System.out.println("余额不足....");
        throw  new BpmnError("payFail");
    }
}

package com.jokls.example.service;

import com.jokls.jok.rpc.annotation.CloudComponent;

@CloudComponent
public class TestService implements ITestService{

    public String startOneTimerTask() {
        return "TestService"+  "startOneTimerTask";
    }
}
package com.jokls.example.service;

import com.jokls.jok.rpc.annotation.CloudService;

@CloudService(
        validation = true
)
public interface ITestService {

    String startOneTimerTask() ;
}
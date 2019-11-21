package com.jokls.example.controller;

import com.jokls.example.service.ITestService;
import com.jokls.example.service.ITestService2;
import com.jokls.jok.rpc.annotation.CloudReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/index")
public class IndexController {
    @CloudReference(service = "testService")
    ITestService testService;

    @CloudReference(service = "iTestService2")
    ITestService2 testService2;

    @RequestMapping("")
    @ResponseBody
    public String index(){
        try {
            return testService.startOneTimerTask()   + "," +testService2.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

}

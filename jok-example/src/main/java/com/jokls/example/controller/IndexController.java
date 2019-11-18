package com.jokls.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("")
    @ResponseBody
    public String index(){
        try {
            return "TestController.index  = ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "test";
    }

}

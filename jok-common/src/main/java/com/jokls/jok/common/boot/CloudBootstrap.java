package com.jokls.jok.common.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 17:10
 */
public class CloudBootstrap {
    public CloudBootstrap() {
    }

    public static String[] prepare(String[]  args) {
        if(args != null && args.length > 0){
            String[] sps = args[0].split("=");
            if(sps.length > 1 && "jfile".equals(sps[0] )){
                return new String[]{"--spring.config.location=" + sps[1]};
            }
        }
        return null;
    }

    public static ConfigurableApplicationContext run(SpringApplication application){
        return run(application,null, null );
    }

    public static ConfigurableApplicationContext run(Object source, String[] args) {
        return run(null, source, args);
    }

    public static ConfigurableApplicationContext run(SpringApplication application, Object source, String[] args) {
        String[] newArgs = prepare(args);

        if (newArgs != null) {
            return application != null ? application.run(newArgs) : SpringApplication.run((Class<?>) source,args);
        } else {
            return application != null ? application.run() : SpringApplication.run((Class<?>) source);
        }
    }
}

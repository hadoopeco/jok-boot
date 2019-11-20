package com.jokls.jok.rpc.monitor;


import com.jokls.jok.rpc.def.RpcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@AutoConfigureAfter({RpcAutoConfiguration.class})
public class RpcMonitorAutoConfiguration {

    public RpcMonitorAutoConfiguration() {
        System.out.println("RpcMonitorAutoConfiguration.RpcMonitorAutoConfiguration running ...");
    }
}

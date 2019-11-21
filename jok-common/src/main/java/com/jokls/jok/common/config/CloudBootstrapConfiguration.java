package com.jokls.jok.common.config;

import com.jokls.jok.common.util.ConfigUtils;
import com.jokls.jok.common.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 * Springboot启动时加载运行， 配置自定义环境变量
 *
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 16:01
 */
public class CloudBootstrapConfiguration implements EnvironmentPostProcessor{
    private static final Logger logger = LoggerFactory.getLogger(CloudBootstrapConfiguration.class);

    private static final String MANAGEMENT_SECURITY_ENABLED = "management.security.enabled";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SERVER_PORT = "server.port";
    private static final String APP_VERSION = "app.version";
    private static final String MANAGEMENT_HEALTH_DEFAULTS_ENABLED = "management.health.defaults.enabled";
    private static final String APP_REGISTRY_ADDRESS = "app.registry.address";
    private static final String APP_GROUP = "app.group";
    private static final String APP_OWNER = "app.owner";
    private static final String COM_ALIPAY_DTX_SDK_CONNS = "com.alipay.dtx.sdk.conns";
    private static final String APP_HOST = "app.host";
    private static final String APP_NAME = "app.name";
    private static final String APP_SERVER_PORT = "app.server.port";
    private static final String RPC_APPLICATION_NAME = "rpc.application.name";
    private static final String RPC_APPLICATION_OWNER = "rpc.application.owner";
    private static final String RPC_REGISTRY_ADDRESS = "rpc.registry.address";
    private static final String APPLICATIONCONFIGURATIONPROPERTIES ="applicationConfigurationProperties";
    public static final String CLOUD_APPLICATION_CONFIG = "cloudApplicationConfig";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            List<String> keys = new ArrayList<>();
            for (PropertySource<?> source : environment.getPropertySources()) {
                if(APPLICATIONCONFIGURATIONPROPERTIES.equals(source.getName())){
                    //获取所有配置项的key值
                    String[] names = ((EnumerablePropertySource)source).getPropertyNames();
                    Collections.addAll(keys, names);
                    break;
                }
            }


            if (keys.size() > 0) {
                addApplicationConfig(environment, keys);
            }

        } catch (Exception e) {
            logger.error("process environment error : {}" , e.getMessage(), e);
        }
    }


    private void addApplicationConfig(ConfigurableEnvironment environment, List<String> keys) throws Exception {
        String appVersion;
        Map<String, Object> map = new HashMap<>();
        boolean alipayDtxSdkConns = false;
        boolean hasHealth = false;
        boolean hasHealthSecurity = false;
        String appGroup = "";
        appVersion = "";
        String appHost = "";
        boolean hasPort = false;

        for (String key : keys) {
            String value = environment.getProperty(key);
            if (!StringUtils.isEmpty(value)) {
                value = ConfigUtils.getEncryptWrappedValue(value);
                switch (key) {
                    case MANAGEMENT_SECURITY_ENABLED :
                        hasHealthSecurity = true;
                        break;
                    case SPRING_APPLICATION_NAME :
                        map.put(SPRING_APPLICATION_NAME, value);
                        map.put(RPC_APPLICATION_NAME, value);
                        break;
                    case SERVER_PORT :
                        hasPort = true;
                        map.put(APP_SERVER_PORT, value);
                        break;
                    case APP_VERSION :
                        appVersion = value;
                        break;
                    case MANAGEMENT_HEALTH_DEFAULTS_ENABLED :
                        hasHealth = true;
                        break;
                    case  APP_REGISTRY_ADDRESS :
                        map.put(RPC_REGISTRY_ADDRESS, value);
                        break;
                    case APP_GROUP :
                        appGroup = value;
                        break;
                    case APP_OWNER :
                        map.put(RPC_APPLICATION_OWNER, value);
                        break;
                    case COM_ALIPAY_DTX_SDK_CONNS :
                        if (Boolean.TRUE.equals(value)) {
                            alipayDtxSdkConns = true;
                        }
                        break;
                    case APP_HOST:
                        appHost = value;
                        break;
                    case APP_NAME:
                        map.put(APP_NAME, value);
                        map.put(RPC_APPLICATION_NAME, value);
                        break;
                    case APP_SERVER_PORT:
                        hasPort = true;
                        map.put(SERVER_PORT, value);
                        break;
                }

                map.put(key, value);
            }
        }

        //如果没有配置appGroup 默认设置为 g
        if (StringUtils.isEmpty(appGroup)) {
            map.put(APP_GROUP, "g");
        }

        //如果没有配置 appVersion默认设置为 v
        if (StringUtils.isEmpty(appVersion)) {
            map.put(APP_VERSION, "v");
        }

        //如果没有配置appHost 默认设置为 *
        if (StringUtils.isEmpty(appHost)) {
            map.put(APP_HOST, "*");
        }

        if (!hasPort) {
            int port = NetUtils.getAvailablePort();
            map.put(SERVER_PORT, port);
            map.put(APP_SERVER_PORT, port);
        }

        map.put(COM_ALIPAY_DTX_SDK_CONNS, alipayDtxSdkConns);
        map.put(MANAGEMENT_HEALTH_DEFAULTS_ENABLED, hasHealth);
        map.put(MANAGEMENT_SECURITY_ENABLED, hasHealthSecurity);
        environment.getPropertySources().addFirst(new MapPropertySource(CLOUD_APPLICATION_CONFIG, map));
    }
}

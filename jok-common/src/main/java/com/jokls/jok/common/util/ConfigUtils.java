package com.jokls.jok.common.util;

import com.jokls.jok.common.boot.CloudConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (C) 2019
 * All rights reserved
 * 当Springboot 启动时加载运行，配置在spring.fatories文件中，
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 11:19
 */
public class ConfigUtils implements EnvironmentPostProcessor {
    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private static final String RPC_PROTOCOL_NAME = "rpc.protocol.name";
    private static final String COM_ALIPAY_SOFA_RPC_BOLT_PORT = "com.alipay.sofa.rpc.bolt.port";
    private static final String CLASSPATH = "classpath:";
    private static final String CLASSPATHWITHSTAR = "classpath*:";
    private static final String RPC_PROTOCOL_PORT = "rpc.protocol.port";
    private static final String RPC_PROTOCOL_CORETHREADS = "rpc.protocol.corethreads";
    private static final String COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_CORE_SIZE = "com.alipay.sofa.rpc.bolt.thread.pool.core.size";
    private static final String RPC_PROTOCOL_THREADS = "rpc.protocol.threads";
    private static final String COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_MAX_SIZE = "com.alipay.sofa.rpc.bolt.thread.pool.max.size";
    private static final String RPC_PROTOCOL_QUEUES = "rpc.protocol.queues";
    private static final String COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_QUEUE_SIZE = "com.alipay.sofa.rpc.bolt.thread.pool.queue.size";
    private static final String RPC_REGISTRY_BIND_NETWORK = "rpc.registry.bindNetwork";
    private static final String COM_ALIPAY_SOFA_RPC_BIND_NETWORK_INTERFACE = "com.alipay.sofa.rpc.bind.network.interface";
    private static final String RPC_REGISTRY_IP_RANGE = "rpc.registry.ipRange";
    private static final String COM_ALIPAY_SOFA_RPC_ENABLED_IP_RANGE = "com.alipay.sofa.rpc.enabled.ip.range";
    private static final String CONFIG_LOCATION = "config.location";
    private static final String APP_NAME = "app.name";
    private static final String APP_ALIAS = "app.alias";
    private static final String APP_OWNER = "app.owner";
    private static final String APP_GROUP = "app.group";
    private static final String APP_VERSION = "app.version";
    private static final String APP_HOST = "app.host";
    private static final String APP_SERVER_PORT = "app.server.port";
    private static final String RPC_VALIDATION_ENABLE = "rpc.validation.enable";
    private static final String RPC_TRACE_LOG = "rpc.trace.log";
    private static final String RPC_MONITOR_ENABLE = "rpc.monitor.enable";
    private static final String RPC_CONSUMER_FORCE_REMOTE = "rpc.consumer.forceRemote";
    private static final String RPC_MOCK_ENABLE = "rpc.mock.enable";
    public static final String LOCATION_CONFIG = "locationConfig";
    public static final String CLASSPATH_ERROR_FORMAT_PROPERTIES = "classpath*:errorFormat/*.properties";
    public static final String CONFIG_ERROR = "config.error";
    private static Environment ENVIRONMENT;


    /**
     *  根据 application.properties 文件中的配置项 config.location 加载具体项目的文件配置
     * @param env
     * @param app
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        if( !StringUtils.isEmpty(env.getProperty(APP_NAME))){
            try{
                Map<String, Object> map = new HashMap<>();
                String configLocations = env.getProperty(CONFIG_LOCATION);
                if(!StringUtils.isEmpty(configLocations)){
                    this.getProperty(map, CloudConstants.COMMA_SPLIT_PATTERN.split(configLocations));
                }

                getPropertyForSofa(map, env);
                ENVIRONMENT = env;
                env.getPropertySources().addLast(new MapPropertySource(LOCATION_CONFIG, map));

                Map<String, Object> errorMap = new HashMap<>();
                String configErrors = env.getProperty(CONFIG_ERROR, CLASSPATH_ERROR_FORMAT_PROPERTIES);
                if (!StringUtils.isEmpty(configErrors)) {
                    this.getProperty(errorMap, CloudConstants.COMMA_SPLIT_PATTERN.split(configErrors));
                }

                ErrorFormatter.getInstance().putErrorMap(errorMap);
            }catch (Exception e){
                logger.error("配置加载异常", e);
            }
        }

    }
    
    private static void getPropertyForSofa(Map<String, Object> map, Environment env) throws Exception {
        String protocol = env.getProperty(RPC_PROTOCOL_NAME);
        if(StringUtils.isEmpty(protocol)){
            protocol = (String)map.get(RPC_PROTOCOL_NAME);
        }

        String bindNetwork;
        String ipRange;
        if(!StringUtils.isEmpty(protocol) && !"bolt".equals(protocol)){
            if(! env.containsProperty(COM_ALIPAY_SOFA_RPC_BOLT_PORT) && !map.containsKey(COM_ALIPAY_SOFA_RPC_BOLT_PORT)){
                map.put(COM_ALIPAY_SOFA_RPC_BOLT_PORT, NetUtils.getAvailablePort());
            }
        } else {
          bindNetwork = env.containsProperty(RPC_PROTOCOL_PORT) ? env.getProperty(RPC_PROTOCOL_PORT): (String)map.get(RPC_PROTOCOL_PORT) ;
          if(!StringUtils.isEmpty(bindNetwork)){
              map.put(COM_ALIPAY_SOFA_RPC_BOLT_PORT,getEncryptWrappedValue(bindNetwork));
          }else if( !env.containsProperty(COM_ALIPAY_SOFA_RPC_BOLT_PORT) && !map.containsKey(COM_ALIPAY_SOFA_RPC_BOLT_PORT)){
              map.put(COM_ALIPAY_SOFA_RPC_BOLT_PORT, NetUtils.getAvailablePort());
          }

          ipRange = env.containsProperty(RPC_PROTOCOL_CORETHREADS) ? env.getProperty(RPC_PROTOCOL_CORETHREADS) : (String)map.get(RPC_PROTOCOL_CORETHREADS);
          if (!StringUtils.isEmpty(ipRange)) {
            map.put(COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_CORE_SIZE, getEncryptWrappedValue(ipRange));
          }

          String threads = env.containsProperty(RPC_PROTOCOL_THREADS) ? env.getProperty(RPC_PROTOCOL_THREADS) : (String)map.get(RPC_PROTOCOL_THREADS);
          if (!StringUtils.isEmpty(threads)) {
             map.put(COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_MAX_SIZE, getEncryptWrappedValue(threads));
          }

          String queues = env.containsProperty(RPC_PROTOCOL_QUEUES) ? env.getProperty(RPC_PROTOCOL_QUEUES) : (String)map.get(RPC_PROTOCOL_QUEUES);
          if (!StringUtils.isEmpty(queues)) {
              map.put(COM_ALIPAY_SOFA_RPC_BOLT_THREAD_POOL_QUEUE_SIZE, getEncryptWrappedValue(queues));
          }
        }

        bindNetwork = env.containsProperty(RPC_REGISTRY_BIND_NETWORK) ? env.getProperty(RPC_REGISTRY_BIND_NETWORK) : (String)map.get(RPC_REGISTRY_BIND_NETWORK);
        if (!StringUtils.isEmpty(bindNetwork)) {
            map.put(COM_ALIPAY_SOFA_RPC_BIND_NETWORK_INTERFACE, getEncryptWrappedValue(bindNetwork));
        }

        ipRange = env.containsProperty(RPC_REGISTRY_IP_RANGE) ? env.getProperty(RPC_REGISTRY_IP_RANGE) : (String)map.get(RPC_REGISTRY_IP_RANGE);
        if (!StringUtils.isEmpty(ipRange)) {
            map.put(COM_ALIPAY_SOFA_RPC_ENABLED_IP_RANGE, getEncryptWrappedValue(ipRange));
        }

    }


    /**
     * 加载确定路径的配置项
     * @param map
     * @param path  文件路径
     * @throws Exception
     */
    private void getPropertyFromPath(Map<String, Object> map, String path) throws Exception{
        File file = new File(path);
        Properties properties = new Properties();
        if( file.isDirectory() ){
            File[] files = file.listFiles();
            if(files != null){
                for(File f :  files){
                    if(f.exists() && f.isFile() && f.getName().endsWith(".propeties")){
                        FileInputStream is = null;

                        try{
                            is = new FileInputStream(f);
                            properties.load(new UnicodeReader(is, "UTF-8"));
                            this.getPropertyFromProperties(map, properties);

                        } catch (Exception e){
                            logger.error("getPropertyFromPath", e);
                        } finally {
                            if(is != null){
                                try{
                                    is.close();
                                }catch (Exception e){
                                    logger.error("getPropertyFromPath inputStream", e);
                                }
                            }
                        }
                    }
                }
            }
        }else {
            FileInputStream is = null;
            try{
                is = new FileInputStream(path);
                properties.load(new UnicodeReader(is, "UTF-8"));
                this.getPropertyFromProperties(map, properties);
            }catch (Exception e){
                logger.error("load properties error",e);
            }finally {
                if( is != null){
                    try{
                        is.close();
                    }catch (Exception e){
                        logger.error("stream close error ",e);
                    }
                }
            }
        }
    }

    /**
     *  遍历 classpath 目录 加载 properties 文件的配置
     * @param map
     * @param locations
     * @throws Exception
     */
    private void getProperty(Map<String, Object> map, String[] locations) throws Exception{
        if(locations != null){
            Resource[] resources;
            for(String location : locations){
                if(location.startsWith(CLASSPATH)){
                    resources = ClassUtils.scanFile(location.replace(CLASSPATH,"").trim());

                    for(Resource resource : resources){
                        this.getPropertyFromResource(map,resource);
                    }
                } else if(location.startsWith(CLASSPATHWITHSTAR)){
                    resources = ClassUtils.scanFile(location.replace(CLASSPATHWITHSTAR,"").trim());

                    for(Resource resource : resources){
                        this.getPropertyFromResource(map, resource);
                    }
                    
                } else {
                    this.getPropertyFromPath(map, location);
                }
            }

        }
    }


    /**
     *  加载配置项
     * @param map
     * @param resource
     * @throws Exception
     */
    private void getPropertyFromResource(Map<String, Object> map, Resource resource) throws Exception{
        InputStream is = null;
        try{
            is = resource.getInputStream();
            Properties pro = new Properties();
            pro.load(new UnicodeReader(is,"UTF-8"));
            this.getPropertyFromProperties(map, pro);
        }catch (Exception e){
            logger.error("get Property from properties",e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    logger.error("InputStream close exception",ex);
                }
            }
        }
    }

    /**
     *  获取所有所有配置项 解密加密字段
     * @param map
     * @param p
     * @throws Exception
     */
    private void getPropertyFromProperties(Map<String, Object> map, Properties p) throws Exception {
        Enumeration e = p.propertyNames();

        while(e.hasMoreElements()) {
            String strKey = (String)e.nextElement();
            String strValue = p.getProperty(strKey);
            if (!map.containsKey(strKey) && !StringUtils.isEmpty(strValue)) {
                map.put(strKey, getEncryptWrappedValue(strValue));
            }
        }

    }

    public static String getEncryptWrappedValue(String value) throws Exception {
        return EncryptUtil.isEncryptWrapped(value) ? EncryptUtil.desDecode(value) : value;
    }

    public static String get(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return ENVIRONMENT.getProperty(key, defaultValue);
    }

    public static <T> T get(String key, Class<T> targetType) {
        return ENVIRONMENT.getProperty(key, targetType);
    }

    public static <T> T get(String key, Class<T> targetType, T defaultValue) {
        return ENVIRONMENT.getProperty(key, targetType, defaultValue);
    }

    public static String getAppName() {
        String name = get(APP_NAME);
        return name == null ? null : name.trim();
    }

    public static String getAppGroup() {
        String version = get(APP_GROUP);
        return "null".equals(version) ? "" : version;
    }

    public static String getAppVersion() {
        String version = get(APP_VERSION);
        return "null".equals(version) ? "" : version;
    }

    public static String getAppAlias() {
        return get(APP_ALIAS);
    }

    public static String getAppOwner() {
        return get(APP_OWNER);
    }

    public static String getAppHost() {
        return get(APP_HOST);
    }

    public static String getServerPort() {
        String port = get(APP_SERVER_PORT);
        return port == null ? "8080" : port;
    }

    public static Boolean isDefaultValidation() {
        try {
            Boolean enable = get(RPC_VALIDATION_ENABLE, Boolean.TYPE);
            return enable == null ? true : enable;
        } catch (Exception e) {
            return true;
        }
    }

    public static Boolean isTraceLog() {
        try {
            Boolean log = get(RPC_TRACE_LOG, Boolean.TYPE);
            return log == null ? false : log;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isRpcMonitorEnable() {
        try {
            Boolean enable = get(RPC_MONITOR_ENABLE, Boolean.TYPE);
            return enable == null ? true : enable;
        } catch (Exception e) {
            return true;
        }
    }

    public static Boolean isRpcForceRemote() {
        try {
            Boolean enable = get(RPC_CONSUMER_FORCE_REMOTE, Boolean.TYPE);
            return enable == null ? false : enable;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isRpcMockEnable() {
        try {
            Boolean enable = get(RPC_MOCK_ENABLE, Boolean.TYPE);
            return enable == null ? false : enable;
        } catch (Exception e) {
            return false;
        }
    }
}

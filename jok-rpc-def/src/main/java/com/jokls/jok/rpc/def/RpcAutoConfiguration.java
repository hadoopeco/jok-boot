package com.jokls.jok.rpc.def;


import com.jokls.jok.common.config.CloudAutoConfiguration;
import com.jokls.jok.common.util.ConfigUtils;
import com.jokls.jok.common.util.NetUtils;
import com.jokls.jok.event.pack.PackV2;
import com.jokls.jok.rpc.api.IRpcContext;
import com.jokls.jok.rpc.api.IRpcProxyFactory;
import com.jokls.jok.rpc.def.config.spring.AnnotationBean;
import com.jokls.jok.rpc.def.context.RpcContextImpl;
import com.jokls.jok.rpc.def.proxy.RpcProxyFactoryImpl;
import com.jokls.jok.rpc.def.trace.ITraceAsync;
import com.jokls.jok.rpc.def.trace.TraceAsyncImpl;
import com.jokls.jok.rpc.t2.util.ServiceDefinitionUtil;
import org.apache.dubbo.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

@Configuration
@EnableAsync
@EnableConfigurationProperties({RpcProperties.class})
@AutoConfigureAfter({CloudAutoConfiguration.class})
/**
 * Rpc auto configure reply on teh annotation
 * **/

public class RpcAutoConfiguration {
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    @Autowired
    private RpcProperties dubboProperties;

    @Bean
    @DependsOn({"springUtils", "serviceDefinitionUtil"})
    public static AnnotationBean annotationBean(){
        String consumerFilters = ConfigUtils.get("rpc.consumer.filters");
        String providerFilters = ConfigUtils.get("rpc.provider.filters");
        return new AnnotationBean(COMMA_SPLIT_PATTERN.split(consumerFilters), COMMA_SPLIT_PATTERN.split(providerFilters), ConfigUtils.get("rpc.protocol.charset", "utf-8"), ConfigUtils.get("rpc.provider.singleMode", Boolean.class, true));
    }

    @Bean
    public ApplicationConfig applicationConfig(){
        if(this.dubboProperties.getApplication() != null) {
            return this.dubboProperties.getApplication();
        }else {
            return new ApplicationConfig();
        }
    }

    @Bean
    public RegistryConfig registryConfig() {
        String registryProtocol = ConfigUtils.get("rpc.registry.protocol", "zookeeper");
        if ("null".equals(registryProtocol)) {
            return null;
        } else {
            RegistryConfig registryConfig = this.dubboProperties.getRegistry();
            if (registryConfig == null) {
                registryConfig = new RegistryConfig();
            }

            registryConfig.setProtocol(registryProtocol);
            registryConfig.setCheck(ConfigUtils.get("rpc.registry.check", Boolean.class, false));
            return registryConfig;
        }
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = this.dubboProperties.getProtocol();
        if (protocolConfig == null) {
            protocolConfig = new ProtocolConfig();
        }

        protocolConfig.setName(ConfigUtils.get("rpc.protocol.name", "t2"));
        protocolConfig.setAccepts(ConfigUtils.get("rpc.protocol.accepts", Integer.class, 1000));
        protocolConfig.setThreads(ConfigUtils.get("rpc.protocol.threads", Integer.class, 500));
        protocolConfig.setCharset(ConfigUtils.get("rpc.protocol.charset", "utf-8"));
        protocolConfig.setCorethreads(ConfigUtils.get("rpc.protocol.corethreads", Integer.class, 300));
//        protocolConfig.setAlive((Integer)ConfigUtils.get("rpc.protocol.alive", Integer.class, 60000));
        if (protocolConfig.getPort() == null) {
            protocolConfig.setPort(NetUtils.getAvailablePort());
        }

        return protocolConfig;
    }

    @Bean
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig) {
        ProviderConfig providerConfig = this.dubboProperties.getProvider();
        if (providerConfig == null) {
            providerConfig = new ProviderConfig();
        }

        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);
        return providerConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig) {
        ConsumerConfig consumerConfig = this.dubboProperties.getConsumer();
        if (consumerConfig == null) {
            consumerConfig = new ConsumerConfig();
        }

        consumerConfig.setCheck(ConfigUtils.get("rpc.consumer.check", Boolean.class, false));
        consumerConfig.setTimeout(ConfigUtils.get("rpc.consumer.timeout", Integer.class, 30000));
        consumerConfig.setLazy(ConfigUtils.get("rpc.consumer.lazy", Boolean.class, false));
        consumerConfig.setRetries(ConfigUtils.get("rpc.consumer.retries", Integer.class, 0));
        consumerConfig.setApplication(applicationConfig);
        consumerConfig.setRegistry(registryConfig);
        return consumerConfig;
    }

    @Bean
    public IRpcProxyFactory rpcProxyFactory(ApplicationConfig application, RegistryConfig registry, ProtocolConfig protocol, ConsumerConfig consumer) {
        String consumerFilters = ConfigUtils.get("rpc.consumer.filters");
        String providerFilters = ConfigUtils.get("rpc.provider.filters");
        return new RpcProxyFactoryImpl(consumerFilters == null ? null : COMMA_SPLIT_PATTERN.split(consumerFilters), providerFilters == null ? null : COMMA_SPLIT_PATTERN.split(providerFilters), application, registry, protocol, consumer, (Boolean)ConfigUtils.get("rpc.proxy.invokeAll", Boolean.class, false));
    }

    @Bean
    public ServiceDefinitionUtil serviceDefinitionUtil() {
        PackV2.useNullMap.put("null_key", ConfigUtils.get("rpc.protocol.t2.nullkey", "true"));
        return new ServiceDefinitionUtil(ConfigUtils.get("rpc.protocol.t2.compress", Boolean.class, false));
    }

    @Bean({"traceTaskExecutor"})
    public Executor traceTaskExecutor() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(ConfigUtils.get("rpc.trace.task.poolsize", Integer.class, 50));
        executor.setThreadNamePrefix("traceTaskExecutor-");
        return executor;
    }

    @Bean
    public ITraceAsync traceAsync() {
        return new TraceAsyncImpl();
    }

    @Bean
    public IRpcContext rpcContext() {
        return new RpcContextImpl();
    }

}

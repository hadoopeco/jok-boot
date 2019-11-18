package com.jokls.jok.rpc.def;


import org.apache.dubbo.config.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "rpc"
)
public class RpcProperties {
    private ApplicationConfig application;
    private RegistryConfig registry;
    private ProtocolConfig protocol;
    private ProviderConfig provider;
    private ConsumerConfig consumer;
    private MonitorConfig monitor;

    public RpcProperties() {
    }

    public ApplicationConfig getApplication() {
        return this.application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public RegistryConfig getRegistry() {
        return this.registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

    public ProtocolConfig getProtocol() {
        return this.protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    public ProviderConfig getProvider() {
        return this.provider;
    }

    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }

    public ConsumerConfig getConsumer() {
        return this.consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }

    public MonitorConfig getMonitor() {
        return this.monitor;
    }

    public void setMonitor(MonitorConfig monitor) {
        this.monitor = monitor;
    }
}

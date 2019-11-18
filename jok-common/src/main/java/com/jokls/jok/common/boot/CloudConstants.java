package com.jokls.jok.common.boot;

import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 17:28
 */
public class CloudConstants {
    public static final String JFILE = "jfile";
    public static final String CONFIG_FILE = "application.properties";
    public static final String GROUP_DEFAULT = "g";
    public static final String VERSION_DEFAULT = "v";
    public static final String HOST_DEFAULT = "*";
    public static final String APP_NAME = "app.name";
    public static final String APP_SERVER_PORT = "app.server.port";
    public static final String APP_GROUP = "app.group";
    public static final String APP_VERSION = "app.version";
    public static final String APP_ALIAS = "app.alias";
    public static final String APP_OWNER = "app.owner";
    public static final String APP_HOST = "app.host";
    public static final String APP_REGISTRY_ADDRESS = "app.registry.address";
    public static final String APP_REFERENCE_PREFIX = "app.reference.";
    public static final String CONFIG_BACKUP = "config.backup";
    public static final String CONFIG_LOCATION = "config.location";
    public static final String CONFIG_ERROR = "config.error";
    public static final String RPC_APPLICATION_NAME = "rpc.application.name";
    public static final String RPC_APPLICATION_OWNER = "rpc.application.owner";
    public static final String RPC_REGISTRY_ADDRESS = "rpc.registry.address";
    public static final String RPC_PROTOCOL_NAME = "rpc.protocol.name";
    public static final String RPC_PROTOCOL_PORT = "rpc.protocol.port";
    public static final String RPC_VALIDATION_ENABLE = "rpc.validation.enable";
    public static final String RPC_PROTOCOL_CORETHREADS = "rpc.protocol.corethreads";
    public static final String RPC_PROTOCOL_THREADS = "rpc.protocol.threads";
    public static final String RPC_PROTOCOL_QUEUES = "rpc.protocol.queues";
    public static final String RPC_REGISTRY_BINDNETWORK = "rpc.registry.bindNetwork";
    public static final String RPC_REGISTRY_IPRANGE = "rpc.registry.ipRange";
    public static final String RPC_TRACE_LOG = "rpc.trace.log";
    public static final String RPC_MONITOR_ENABLE = "rpc.monitor.enable";
    public static final String RPC_MONITOR_COLLECT_TYPE = "rpc.monitor.collect.type";
    public static final String RPC_PROVIDER_FILTERS = "rpc.provider.filters";
    public static final String RPC_CONSUMER_FILTERS = "rpc.consumer.filters";
    public static final String RPC_CONSUMER_TIMEOUT = "rpc.consumer.timeout";
    public static final String RPC_CONSUMER_FORCEREMOTE = "rpc.consumer.forceRemote";
    public static final String RPC_MOCK_ENABLE = "rpc.mock.enable";
    public static final String RPC_MOCK_ADDRESS = "rpc.mock.address";
    public static final String COM_ALIPAY_DTX_SDK_CONNS = "com.alipay.dtx.sdk.conns";
    public static final String RPC_PROTOCOL_PORT_SOFA = "com.alipay.sofa.rpc.bolt.port";
    public static final String RPC_PROTOCOL_CORETHREADS_SOFA = "com.alipay.sofa.rpc.bolt.thread.pool.core.size";
    public static final String RPC_PROTOCOL_THREADS_SOFA = "com.alipay.sofa.rpc.bolt.thread.pool.max.size";
    public static final String RPC_PROTOCOL_QUEUES_SOFA = "com.alipay.sofa.rpc.bolt.thread.pool.queue.size";
    public static final String RPC_REGISTRY_BINDNETWORK_SOFA = "com.alipay.sofa.rpc.bind.network.interface";
    public static final String RPC_REGISTRY_IPRANGE_SOFA = "com.alipay.sofa.rpc.enabled.ip.range";
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";
    public static final String SERVER_PORT = "server.port";
    public static final String MANAGEMENT_HEALTH_DEFAULTS_ENABLED = "management.health.defaults.enabled";
    public static final String MANAGEMENT_HEALTH_SECURITY_ENABLED = "management.security.enabled";
    public static final String PROTOCOL_TYPE_BOLT = "bolt";
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    public static final Pattern BRACE_SPLIT_PATTERN = Pattern.compile("\\{.*?\\}");

    public CloudConstants() {
    }
}

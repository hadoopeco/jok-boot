package com.jokls.jok.rpc.monitor.condition;

import com.jokls.jok.common.util.ConfigUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MonitorEnableEndpoint implements Condition {


    /**
     * judge the Rpc condition configuration
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Boolean.TRUE.equals(ConfigUtils.isRpcMonitorEnable());
    }
}

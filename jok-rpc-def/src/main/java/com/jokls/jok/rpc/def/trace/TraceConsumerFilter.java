package com.jokls.jok.rpc.def.trace;

import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.rpc.constant.RpcConstants;
import com.jokls.jok.common.util.*;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.monitor.MonitorService;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Activate(
        group = {"consumer"},
        order = -2147483647
)
public class TraceConsumerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TraceConsumerFilter.class);
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        Result  result = invoker.invoke(invocation);
        Map<String, Object> values = RpcContext.getContext().get();
        String debugType = (String)values.get(RpcConstants.DEBUGTYPE_KEY);
        if(!MonitorService.class.getName().equalsIgnoreCase(invoker.getInterface().getName()) && Boolean.TRUE.equals(ConfigUtils.isTraceLog() || "1".equals(debugType))){
            TraceInfo trace = (TraceInfo)values.get(RpcConstants.CURRENT_TRACE_KEY);
            boolean canTrace = true;
            if (trace != null){
                boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);
                TraceInfo newTrace = new TraceInfo();

                try{
                    newTrace.setTraceId(trace.getTraceId());
                    newTrace.setDepotId("r0");
                    newTrace.setSpanId(trace.getSpanId() +'.' + trace.atomicSpan());
                    newTrace.setTimestamp(DateUtils.getDateStringNow());
                    Map<String, String> attachments = invocation.getAttachments();
                    attachments.put(RpcConstants.TRACE_ID_KEY, newTrace.getTraceId());
                    attachments.put(RpcConstants.DEPOT_ID_KEY, newTrace.getDepotId());
                    attachments.put(RpcConstants.SPAN_ID_KEY, newTrace.getSpanId());

                    if(isAsync) {
                        newTrace.setName(ConfigUtils.getAppName());
                        newTrace.setServerName(RpcUtils.getMethodName(invocation));
                        newTrace.setIp(NetUtils.getLocalHost());
                        newTrace.setAppType("jres-svr");
                        newTrace.setSpanType("1");
                        values.put(RpcConstants.CURRENT_TRACE_ASYNC_KEY, newTrace);
                    }
                }catch (Exception e){
                    logger.warn("获取全链路信息异常", e);
                    canTrace = false;
                }

                String depotId ;
                BaseException exception  = null;

                if(canTrace){
                    depotId = result.getAttachment(RpcConstants.DEPOT_ID_KEY);
                    if(StringUtils.isEmpty(depotId)){
                        depotId = "a0";
                    }

                    if (result.hasException()) {
                        exception = ExceptionUtils.getBaseException(result.getException(), -1);
                    }

                    SpringUtils.getBean(ITraceAsync.class).logConsumer(newTrace, invoker, invocation, depotId, exception, isAsync, null);
                }

            }
        }

        return result;

    }
}

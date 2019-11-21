package com.jokls.jok.rpc.def.trace;

import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.common.util.*;
import com.jokls.jok.rpc.constant.RpcConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.NetUtils;
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
    public static final String JOK_SVR = "jok-svr";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = invocation.getAttachments();
        Result  result = invoker.invoke(invocation);
        Map<String, Object> values = RpcContext.getContext().get();
        String debugType = (String)values.get(RpcConstants.DEBUGTYPE_KEY);

        if( Boolean.TRUE.equals(ConfigUtils.isTraceLog() || RpcConstants.DEBUGTYPE_TRACE.equals(debugType) )){
            TraceInfo trace = (TraceInfo)values.get(RpcConstants.CURRENT_TRACE_KEY);
            if(trace == null){
                trace = new TraceInfo();
                trace.setTraceId(TraceUtils.generatorTraceId());
            }


            boolean canTrace = true;
            boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);

            TraceInfo newTrace = new TraceInfo();

            try{
                newTrace.setTraceId(trace.getTraceId());
                newTrace.setDepotId("r0");
                newTrace.setSpanId(trace.getSpanId() +'.' + trace.atomicSpan());
                newTrace.setTimestamp(DateUtils.getDateStringNow());
                attachments.put(RpcConstants.TRACE_ID_KEY, newTrace.getTraceId());
                attachments.put(RpcConstants.DEPOT_ID_KEY, newTrace.getDepotId());
                attachments.put(RpcConstants.SPAN_ID_KEY, newTrace.getSpanId());

                values.put(RpcConstants.CURRENT_TRACE_KEY, trace);
                if(isAsync) {
                    newTrace.setName(ConfigUtils.getAppName());
                    newTrace.setServerName(RpcUtils.getMethodName(invocation));
                    newTrace.setIp(NetUtils.getLocalHost());
                    newTrace.setAppType(JOK_SVR);
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
                    depotId = "d";
                }

                if (result.hasException()) {
                    exception = ExceptionUtils.getBaseException(result.getException(), -1);
                }
                //log the consumer invocation message
                SpringUtils.getBean(ITraceAsync.class).logConsumer(newTrace, invoker, invocation, depotId, exception, isAsync, null);
            }

        }
        return result;

    }
}

package com.jokls.jok.rpc.def.trace;

import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.common.util.*;
import com.jokls.jok.rpc.async.AsyncFuture;
import com.jokls.jok.rpc.constant.RpcConstants;
import com.jokls.jok.rpc.def.asyn.DefaultAsyncFuture;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.monitor.MonitorService;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Activate(
        group = {"provider"},
        order = -2147483647
)
public class TraceProviderFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TraceProviderFilter.class);


    public TraceProviderFilter() {
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, String> attachments = invocation.getAttachments();
        String debugType = attachments.get(RpcConstants.DEBUGTYPE_KEY);
        Result result;
        if(!Boolean.TRUE.equals(ConfigUtils.isTraceLog()) && RpcConstants.DEBUGTYPE_TRACE.equals(debugType)){
            result = invoker.invoke(invocation);
        }else {
            boolean canTrace = true;
            TraceInfo newTrace = new TraceInfo();

            try{
                String traceId = attachments.get(RpcConstants.TRACE_ID_KEY);
                String depotId = attachments.get(RpcConstants.DEPOT_ID_KEY);
                String spanId = attachments.get(RpcConstants.SPAN_ID_KEY);

                if (StringUtils.isEmpty(traceId)){
                    newTrace.setTraceId(TraceUtils.generatorTraceId());
                }else{
                    newTrace.setTraceId(traceId);
                }

                if(StringUtils.isEmpty(depotId)){
                    newTrace.setDepotId("r0");
                }else {
                    newTrace.setDepotId(depotId.toLowerCase());
                }


                if(StringUtils.isEmpty(spanId)){
                    newTrace.setSpanId("0");
                }else {
                    newTrace.setSpanId(spanId);
                }


                newTrace.setTimestamp(DateUtils.getDateStringNow());

                DefaultAsyncFuture asyncFuture = (DefaultAsyncFuture) RpcContext.getContext().get(AsyncFuture.class.getName());

                if(asyncFuture != null){
                    asyncFuture.setTrace(newTrace);
                }

                if (!StringUtils.isEmpty(debugType)) {
                    RpcContext.getContext().set(RpcConstants.DEBUGTYPE_KEY, debugType);
                }
            } catch (Exception e) {
                logger.warn("获取全链路信息异常", e);
                canTrace = false;
            }

            BaseException exception = null;

            try{
                result = invoker.invoke(invocation);

                if(result.hasException()){
                    exception = ExceptionUtils.getBaseException(result.getException(), -1);
                }
            }catch (Exception e){
                exception = ExceptionUtils.getBaseException(e, 2147483647);
                throw e;
            }finally {
                if(canTrace){

                    SpringUtils.getBean(ITraceAsync.class).logProvider(newTrace, invoker, invocation, exception);
                }

                attachments.put(RpcConstants.TRACE_ID_KEY, newTrace.getTraceId());
                attachments.put(RpcConstants.DEPOT_ID_KEY, "a0");
                attachments.put(RpcConstants.SPAN_ID_KEY, newTrace.getSpanId());
            }

        }
        return result;
    }
}

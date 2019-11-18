package com.jokls.jok.rpc.def.trace;

import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.rpc.def.util.RpcUtils;
import com.jokls.jok.common.util.*;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class TraceAsyncImpl implements ITraceAsync {
    private static final Logger logger = LoggerFactory.getLogger(TraceAsyncImpl.class);

    @Async("traceTaskExecutor")
    public void logConsumerAsync(TraceInfo newTrace, String depotId, BaseException baseException, byte[] msgBody, String charset) {
        newTrace.setDepotId(depotId);
        newTrace.setTimestamp(DateUtils.getDateStringNow());
        newTrace.setModuleType("CR");

        if(!StringUtils.isEmpty(charset) && !"utf-8".equalsIgnoreCase(charset.trim())){
            newTrace.setCharset("0");
        } else {
            newTrace.setCharset("1");
        }

        if (baseException != null){
            newTrace.setResponseStatus("1");
            newTrace.setMyPackage("{\"error_no\":\"" + baseException.getErrorCode() + "\",\"error_info\":\"" + baseException.getErrorMessage() + "\"}");
        } else {
            newTrace.setResponseStatus("0");
            if (msgBody != null){
                newTrace.setMyPackage("{\"message\":\"" + Base64.encode(msgBody) + "\"}");
            }
        }

        logger.info(newTrace.toString());
    }


    @Async("traceTaskExecutor")
    public void logConsumer(TraceInfo newTrace, Invoker<?> invoker, Invocation invocation, String depotId, BaseException baseException, boolean isAsync, byte[] msgBody) {
        String nowStr = DateUtils.getDateStringNow();
        newTrace.setName(ConfigUtils.getAppName());
        newTrace.setServerName(RpcUtils.getFunctionName(invoker, invocation));
        newTrace.setIp(NetUtils.getLocalHost());
        newTrace.setAppType("jres-svr");
        newTrace.setSpanType("1");
        newTrace.setModuleType("CS");

//        if("utf-8".equalsIgnoreCase(invoker.getUrl().getParameter("charset", AnnotationBean.getTransCharset()).trim()));
        newTrace.setCharset("0");


        if(!isAsync){
            newTrace.setDepotId(depotId);
            newTrace.setTimestamp(nowStr);
            newTrace.setModuleType("CR");

            if (baseException != null) {
                newTrace.setResponseStatus("1");
                newTrace.setMyPackage("{\"error_no\":\"" + baseException.getErrorCode() + "\",\"error_info\":\"" + baseException.getErrorMessage() + "\"}");
            } else {
                newTrace.setResponseStatus("0");
                if (msgBody != null) {
                    newTrace.setMyPackage("{\"message\":\"" + Base64.encode(msgBody) + "\"}");
                }
            }
        }

        logger.info(newTrace.toString());
    }

    @Async("traceTaskExecutor")
    public void logProvider(TraceInfo newTrace, Invoker<?> invoker, Invocation invocation, BaseException baseException) {
        String nowStr = DateUtils.getDateStringNow();
        newTrace.setName(ConfigUtils.getAppName());
        newTrace.setServerName(RpcUtils.getFunctionName(invoker, invocation));
        newTrace.setIp(NetUtils.getLocalHost());
        newTrace.setModuleType("SR");
        newTrace.setAppType("jres-svr");
        newTrace.setSpanType("1");


        newTrace.setCharset("0");


        newTrace.setMyPackage(null);
        newTrace.setDepotId("a0");
        newTrace.setTimestamp(nowStr);
        newTrace.setModuleType("SS");
        if (baseException != null) {
            newTrace.setResponseStatus("1");
            newTrace.setMyPackage("{\"error_no\":\"" + baseException.getErrorCode() + "\",\"error_info\":\"" + baseException.getErrorMessage() + "\"}");
        } else {
            newTrace.setResponseStatus("0");
        }

        logger.info(newTrace.toString());

    }
}

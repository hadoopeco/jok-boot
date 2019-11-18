package com.jokls.jok.rpc.def.asyn;

import com.jokls.jok.common.exception.BaseException;
import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.common.util.ExceptionUtils;
import com.jokls.jok.common.util.SpringUtils;
import com.jokls.jok.rpc.async.AsyncFuture;
import com.jokls.jok.rpc.def.monitor.IMonitorAsync;
import com.jokls.jok.rpc.def.trace.ITraceAsync;
import com.jokls.jok.rpc.exception.BaseRpcException;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.exchange.ExchangeChannel;
import org.apache.dubbo.remoting.exchange.Response;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 18:01
 */
public class DefaultAsyncFuture  implements AsyncFuture {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAsyncFuture.class);
    private Invocation inv;
    private Invoker<?> invoker;
    private TraceInfo trace;
    private Long beginTime;
    private final ExchangeChannel channel;

    public DefaultAsyncFuture(Invoker<?> invoker, Invocation inv, ExchangeChannel channel) {
        this.inv = inv;
        this.channel = channel;
        this.invoker = invoker;
    }


    @Override
    public void set(Object value) {
        if(this.beginTime != null){
            try{
                SpringUtils.getBean(IMonitorAsync.class).statProvider(this.invoker,this.inv,null, System.currentTimeMillis());
            } catch (Exception e){
                logger.warn("监控统计异常",e);
            }
        }

        if(this.trace != null){
            try{
                SpringUtils.getBean(ITraceAsync.class).logProvider(this.trace, this.invoker, this.inv,null);
            }catch (Exception e){
                logger.warn("全链路信息输出异常", e);
            }
        }

        Result result = new AsyncRpcResult(this.inv);
        result.setValue(value);
        this.send(result);
    }

    @Override
    public void set(Throwable exception) {
        BaseException baseException = ExceptionUtils.getBaseException(exception, -1);
        if(this.beginTime != null) {
            try {
                SpringUtils.getBean(IMonitorAsync.class).statProvider(this.invoker, this.inv, baseException.getErrorCode(), null);
            } catch (Exception e) {
                logger.warn("监控统计异常", e);
            }
        }

        if(this.trace != null){
            try{
                SpringUtils.getBean(ITraceAsync.class).logProvider(this.trace,this.invoker, this.inv, baseException);
            }catch (Exception e){
                logger.warn("全链路信息输出异常", e);
            }
        }

        Result result = new AsyncRpcResult(this.inv);
        result.setValue(exception);
        this.send(result);
    }

    private void send(Result rs){
        Response res = new Response();
        res.setStatus((byte) 20);
        rs.setAttachments(RpcContext.getContext().getAttachments());

        res.setResult(rs);

        try{
            this.channel.send(res);
        }catch (RemotingException e) {
            throw new BaseRpcException(2608, "通道异常!");
        }
    }

    public TraceInfo getTrace() {
        return this.trace;
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public Long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }
}

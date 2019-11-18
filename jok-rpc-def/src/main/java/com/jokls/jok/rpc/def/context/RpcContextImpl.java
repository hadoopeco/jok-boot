package com.jokls.jok.rpc.def.context;

import com.jokls.jok.common.trace.TraceInfo;
import com.jokls.jok.rpc.api.IRpcContext;
import com.jokls.jok.rpc.async.AsyncFuture;
import com.jokls.jok.rpc.exception.BaseRpcException;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.Future;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 15:46
 */
public class RpcContextImpl implements IRpcContext {
    @Override
    public void putRequestBaggage(String key, String value) {
        if(key == null){
            throw new BaseRpcException(2606, "隐式传参Key 不能为null！");
        } else {
            String keyTrim = key.trim();
            if(!"path".equals(keyTrim) && !"version".equals(keyTrim) && !"interface".equals(keyTrim) && !"group".equals(keyTrim) && !keyTrim.startsWith("trace.")){
                RpcContext.getContext().setAttachment(key,value);
            }else {
                throw new BaseRpcException(2606, keyTrim + "包含系统关键字，不能作为隐式传参的key！");
            }
        }

    }

    @Override
    public void putResponseBaggage(String key, String value) {
        if (key == null) {
            throw new BaseRpcException(2606, "隐式传参key不能为null！");
        } else {
            String keyTrim = key.trim();
            if (!"path".equals(keyTrim) && !"version".equals(keyTrim) && !"interface".equals(keyTrim) && !"group".equals(keyTrim) && !keyTrim.startsWith("trace.")) {
                RpcContext.getContext().setAttachment(key, value);
            } else {
                throw new BaseRpcException(2606, keyTrim + "包含系统关键字，不能作为隐式传参的key！");
            }
        }
    }

    public String getRequestBaggage(String key) {
        return RpcContext.getContext().getAttachment(key);
    }

    public String getResponseBaggage(String key) {
        return RpcContext.getContext().getAttachment(key);
    }

    public Future getFuture() {
        return RpcContext.getContext().getFuture();
    }

    public AsyncFuture getAsyncFuture() {
        return (AsyncFuture) RpcContext.getContext().get(AsyncFuture.class.getName());
    }

    public void set(String key, Object value) {
        RpcContext.getContext().set(key, value);
    }

    public Object get(String key) {
        return RpcContext.getContext().get(key);
    }

    public void setTrace(TraceInfo traceInfo) {
        RpcContext.getContext().set("trace.currentTrace", traceInfo);
    }
}

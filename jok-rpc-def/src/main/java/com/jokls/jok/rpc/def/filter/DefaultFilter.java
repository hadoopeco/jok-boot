package com.jokls.jok.rpc.def.filter;

import com.jokls.jok.rpc.exception.BaseRpcException;
import com.jokls.jok.rpc.filter.FilterContext;
import com.jokls.jok.rpc.filter.RpcFilter;
import org.apache.dubbo.rpc.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 16:33
 */
public class DefaultFilter implements Filter {
    private RpcFilter rpcFilter;

    public DefaultFilter(RpcFilter rpcFilter) {
        this.rpcFilter = rpcFilter;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if(this.rpcFilter.needToLoad()){
            FilterContext filterContext = new FilterContext();
            filterContext.setInterfaceName(invoker.getInterface().getName());
            filterContext.setMethodArgs(invocation.getArguments());
            filterContext.setMethodName(invocation.getMethodName());

//            filterContext.setMethod(invocation.get);

            this.rpcFilter.before(filterContext);
            Result result = invoker.invoke(invocation);
            if(result == null){
                this.rpcFilter.after(new BaseRpcException(2601, "空响应"), filterContext);
            }

            if (result != null && result.hasException()) {
                this.rpcFilter.after(result.getException(), filterContext);
            } else {
                this.rpcFilter.after(result.getValue(), filterContext);
            }

            return result;

        }else {
            return invoker.invoke(invocation);
        }
    }
}

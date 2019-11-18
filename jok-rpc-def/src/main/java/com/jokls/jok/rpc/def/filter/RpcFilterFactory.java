package com.jokls.jok.rpc.def.filter;

import com.jokls.jok.rpc.exception.BaseRpcException;
import com.jokls.jok.rpc.filter.RpcFilter;
import org.apache.dubbo.rpc.Filter;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 16:23
 */
public class RpcFilterFactory {
    private Map<String, Filter> filters = new HashMap<>();
    private static RpcFilterFactory INSTANCE = new RpcFilterFactory();

    public RpcFilterFactory() {
    }

    public static RpcFilterFactory getInstance(){return INSTANCE;}

    public Filter getFilter(String className){
        Filter filter = this.filters.get(className);
        if(filter == null){
            synchronized (this.filters){
                filter =  this.filters.get(className);
                if(filter == null){
                   try{
                       filter = new DefaultFilter((RpcFilter)Class.forName(className).newInstance());
                       this.filters.put(className, filter);
                   }catch (Exception e) {
                       throw new BaseRpcException(2604, e, "获取过滤器实例失败！");
                   }
                }
            }
        }
        return filter;
    }


}

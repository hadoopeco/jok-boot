package com.jokls.jok.rpc.def.util;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.rpc.t2.base.ServiceDefinition;
import com.jokls.jok.rpc.t2.util.ServiceDefinitionUtil;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.jokls.jok.rpc.constant.RpcConstants.FUNCTIONID_KEY;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/27 17:57
 */
public class RpcUtils {
    public static final String SERVICE_FILTER_KEY = ".service";
    public static final String ADDRESS_FILTE_KYE = ".address";
    public static final String ID_FILTER_KEY = ".id";

    private final static Logger logger = LoggerFactory.getLogger(RpcUtils.class);

    public RpcUtils(){}

    public static String getFunctionName(Invoker invoker, Invocation invocation){
        String functionId  = invocation.getAttachment(FUNCTIONID_KEY);
        if(!StringUtils.isEmpty(functionId)){
            return functionId;
        } else {
            ServiceDefinition def = null;
            if("$invoke".equals(invocation.getMethodName())){
                def = ServiceDefinitionUtil.getServiceDefinition(null, (String)invocation.getArguments()[0]);
            }

            return def != null && !StringUtils.isEmpty(def.getFunctionId()) ? def.getFunctionId() : String.format("%s_%s", invoker.getInterface().getSimpleName(), invocation.getMethodName());
        }
    }

    public static String postMockServer(String url, String jsonParam){
        HttpClient client = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String respStr = "";

        try{
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            post.setEntity(entity);
            client = HttpClientBuilder.create().build();
            reader = new InputStreamReader(client.execute(post).getEntity().getContent(), "UTF-8");
            br = new BufferedReader(reader);
            String line = null;
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            respStr = sb.toString();
        } catch (IOException e) {
            logger.error("postMockServer error ",e);
        } finally {


            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("reader close error ",e);
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("buffer reader close error ",e);
                }
            }
        }
        return respStr;
    }
}

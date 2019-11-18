package com.jokls.jok.rpc.api;

import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 15:50
 */
public interface IRpcProxyFactory {
    <T> T proxyInvoke(Class<T> serviceInterface);

    <T> T proxyInvoke(Class<T> serviceInterface, String group);

    <T> T proxyInvoke(Class<T> serviceInterface, String group, String version);

    <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters);

    <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters, String charset);

    <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters, String charset, String version);

    void proxyService(Object bean, Class<?> serviceInterface, Class<?>[] filters);

    String genericInvokeJresT3(String interfaceName, String group, String version, String functionId, String jsonParam);

    void genericInvokeJresT3ByCallback(String interfaceName, String group, String version, String functionId, String jsonParam, Class<?> callbackClass);

    <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface);

    <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group);

    <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, String version);

    <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, Integer timeout, Class<?>[] filters);

    <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, Integer timeout, Class<?>[] filters, String version);

}

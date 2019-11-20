package com.jokls.jok.rpc.def.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jokls.jok.common.exception.BaseCommonException;
import com.jokls.jok.common.util.ConfigUtils;
import com.jokls.jok.common.util.JsonUtils;
import com.jokls.jok.rpc.api.IRpcProxyFactory;
import com.jokls.jok.rpc.t2.base.ServiceDefinition;
import com.jokls.jok.rpc.t2.util.ServiceDefinitionUtil;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.RegistryService;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RpcProxyFactoryImpl implements IRpcProxyFactory, InitializingBean, DisposableBean, NotifyListener {

    private String[] consumerFilters;
    private String[] providerFilters;
    // dubbo:application  配置信息
    // <dubbo:application name="etrading" owner="etrading" id="etrading" />
    private ApplicationConfig application;
    // dubbo:registry  注册中心信息
    // <dubbo:registry address="10.20.24.240:2313" protocol="zookeeper" group="dubbo" check="false" />
    private RegistryConfig registry;
    // dubbo:protocol  dubbo 配置协议信息
    // <dubbo:protocol name="dubbo" threads="500" port="64268" charset="utf-8" corethreads="300" alive="60000" accepts="1000" id="dubbo" />
    private ProtocolConfig protocol;
    // dubbo:consumer 消费配置
    //<dubbo:consumer generic="false" check="false" lazy="false" timeout="30000" retries="0" />
    private ConsumerConfig consumer;

    private RegistryService registryService;

    public static final String SERVICE_FILTER_KEY = ".service";
    private static String DEFAULT_TRANS_CHARSET = "UTF-8";
    private static final URL SUBSCRIBE = new URL("admin", NetUtils.getLocalHost(), 0, "", "interface", "*", "group", "*", "version", "*", "classifier", "*", "category", "providers,consumers,routers,configurators", "enabled", "*", "check", String.valueOf(false));
    private final ConcurrentHashMap<String, Map<String, Map<Long, URL>>> registryCache = new ConcurrentHashMap<>();
    private static final AtomicLong ID = new AtomicLong();

    public RpcProxyFactoryImpl(String[] consumerFilters, String[] providerFilters, ApplicationConfig application, RegistryConfig registry, ProtocolConfig protocol, ConsumerConfig consumer, boolean invokeAll) {
        this.consumerFilters = consumerFilters;
        this.providerFilters = providerFilters;
        this.application = application;
        this.registry = registry;
        this.protocol = protocol;
        this.consumer = consumer;

        if (invokeAll) {
            this.registryService = this.proxyInvoke(RegistryService.class);
        }

    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface) {
        return this.proxyInvoke(serviceInterface, null, null, null, null, null);
    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface, String group) {
        return this.proxyInvoke(serviceInterface, group, null, null, null, null);
    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface, String group, String version) {
        return this.proxyInvoke(serviceInterface, group, null, null, null, null, null, version);
    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters) {
        return this.proxyInvoke(serviceInterface, group, timeout, targetUrl, protocol, filters, null);
    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters, String charset) {
        return this.proxyInvoke(serviceInterface, group, timeout, targetUrl, protocol, filters, charset, null);
    }

    @Override
    public <T> T proxyInvoke(Class<T> serviceInterface, String group, Integer timeout, String targetUrl, String protocol, Class<?>[] filters, String charset, String version) {
        StringBuilder allfilters = new StringBuilder();
        if(this.consumerFilters != null){
            String consumerStrs = String.join(",", this.consumerFilters);
            allfilters.append(consumerStrs).append(",");
        }

        if(filters != null){
            String filterStrs = Arrays.stream(filters).map(Class::getName).collect(Collectors.joining(","));
            allfilters.append(filterStrs);
        }

        String serviceGroup = null;
        if (!StringUtils.isEmpty(group)) {
            if (!"null".equals(group)) {
                serviceGroup = group;
            }
        } else {
            serviceGroup = ConfigUtils.getAppGroup();
        }

        String serviceVersion = null;
        if (!StringUtils.isEmpty(version)) {
            if (!"null".equals(version)) {
                serviceVersion = version;
            }
        } else {
            serviceVersion = ConfigUtils.getAppVersion();
        }

        Integer timeoutValue = null;
        if (timeout != null) {
            timeoutValue = timeout;
        } else if (this.consumer != null && this.consumer.getTimeout() != null) {
            timeoutValue = this.consumer.getTimeout();
        }

        ReferenceConfig<T> reference = new ReferenceConfig<>();
        String targetUrlValue = null;
        if (!StringUtils.isEmpty(targetUrl)) {
            targetUrlValue = targetUrl;
//            todo: new API had been remove the field;
//            reference.setCharset(charset);
        }

        reference.setApplication(this.application);
        reference.setRegistry(this.registry);
        reference.setConsumer(this.consumer);
        reference.setInterface(serviceInterface);
        reference.setScope("remote");
        reference.setGroup(serviceGroup);
        reference.setVersion(serviceVersion);
        reference.setTimeout(timeoutValue);
        reference.setUrl(targetUrlValue);
//        reference.setReffilters(filtersLists.toString());

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        T object = cache.get(reference);
        if (object != null) {
            Method[] typeMethods = serviceInterface.getMethods();

            for(Method typeMethod : typeMethods) {
                if (ServiceDefinitionUtil.getServiceDefContainer().getServiceDefinition(typeMethod) == null) {
                    ServiceDefinition serviceDefinition = ServiceDefinitionUtil.parse(typeMethod, null);
                    if (serviceDefinition != null) {
                        ServiceDefinitionUtil.getServiceDefContainer().addDefinition(serviceDefinition);
                    }
                }
            }
        }

        return object;
    }

    @Override
    public void proxyService(Object bean, Class<?> serviceInterface, Class<?>[] filters) {
        StringBuffer filtersb =  new StringBuffer();
        if(this.providerFilters != null ){
            filtersb.append(String.join(",",this.providerFilters)).append(",");
        }

        if(filters != null){
            filtersb.append(Arrays.stream(filters).map(Class::getName).collect(Collectors.joining(",")));
        }

        String serviceGroup = null;
        if (!"null".equals(ConfigUtils.getAppGroup())) {
            serviceGroup = ConfigUtils.getAppGroup();
        }

        String serviceVersion = null;
        if (!"null".equals(ConfigUtils.getAppVersion())) {
            serviceVersion = ConfigUtils.getAppVersion();
        }

        ServiceConfig<Object> service = new ServiceConfig<>();
        service.setApplication(this.application);
        service.setRegistry(this.registry);
        service.setProtocol(this.protocol);
        service.setInterface(serviceInterface);
        service.setRef(bean);
        service.setVersion(serviceVersion);
        service.setGroup(serviceGroup);
        service.export();
    }

    @Override
    public String genericInvokeJresT3(String interfaceName, String group, String version, String functionId, String jsonParam) {
        ReferenceConfig<GenericService> refrence = new ReferenceConfig<>();
        refrence.setApplication(this.application);
        refrence.setRegistry(this.registry);
        refrence.setConsumer(this.consumer);
        refrence.setInterface(interfaceName);
        if(!"null".equals(version)){
            refrence.setVersion(version);
        }

        if(!"null".equals(group)){
            refrence.setGroup(group);
        }
        refrence.setGeneric(true);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(refrence);


        Object result =genericService.$invoke(functionId, null, new Object[]{jsonParam});
        if(result instanceof String){
            return (String)result;
        }else{
            try{
                return JsonUtils.getObjectMapper().writeValueAsString(result);
            }catch (JsonProcessingException e){
                throw new BaseCommonException(-5, e, "序列化为字符串失败！");
            }
        }
    }

    @Override
    public void genericInvokeJresT3ByCallback(String interfaceName, String group, String version, String functionId, String jsonParam, Class<?> callbackClass) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(this.application);
        reference.setRegistry(this.registry);
        reference.setConsumer(this.consumer);
        reference.setInterface(interfaceName);
//        reference.setCallType("callback");
        reference.setAsync(true);
//        reference.setCallbackclass(callbackClass.getName());
        if (!"null".equals(version)) {
            reference.setVersion(version);
        }

        if (!"null".equals(group)) {
            reference.setGroup(group);
        }


        reference.setGeneric(true);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);
        genericService.$invoke(functionId,null, new Object[]{jsonParam});
    }

    @Override
    public <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface) {
        return this.proxyInvokeAll(serviceInterface, null, null, null, null);
    }

    @Override
    public <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group) {
        return this.proxyInvokeAll(serviceInterface, group, null, null, null);
    }

    @Override
    public <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, String version) {
        return this.proxyInvokeAll(serviceInterface, group, null, null, version);
    }

    @Override
    public <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, Integer timeout, Class<?>[] filters) {
        return this.proxyInvokeAll(serviceInterface, group, timeout, filters, null);
    }

    @Override
    public <T> Map<String, T> proxyInvokeAll(Class<T> serviceInterface, String group, Integer timeout, Class<?>[] filters, String version) {
        String serviceGroup = null;
        if(!StringUtils.isEmpty(group)){
            if(!"null".equals(group)){
                serviceGroup = group;
            }
        }else if (!StringUtils.isEmpty(ConfigUtils.getAppGroup())){
            serviceGroup = ConfigUtils.getAppGroup();
        }

        String serviceVersion = null;
        if(!StringUtils.isEmpty(version)){
            if(!"null".equals(version)){
                serviceVersion = version;
            }
        } else if(!StringUtils.isEmpty(ConfigUtils.getAppVersion())){
            serviceVersion = ConfigUtils.getAppVersion();
        }

        Map<String, T> reObjectMap = new HashMap<>();
        List<URL> urls = this.findAddressesByService(this.getServiceKey(serviceInterface.getName(),serviceVersion,serviceGroup));

        if(!urls.isEmpty()){
            final String finalServiceGroup = serviceGroup;
            final String finalversion = serviceVersion;
            urls.forEach( u -> reObjectMap.put(u.getAddress(), this.proxyInvoke(serviceInterface, finalServiceGroup, timeout, u.toString(),null, filters, u.getParameter("charset", DEFAULT_TRANS_CHARSET), finalversion)));
        }

        return reObjectMap;
    }


    @Override
    public void notify(List<URL> urls) {
        if (urls != null && !urls.isEmpty()) {
            Map<String, Map<String, Map<Long, URL>>> categories = new HashMap<>();
            for (URL url : urls) {
                String category = url.getParameter("category", "providers");
                if ("empty".equalsIgnoreCase(url.getProtocol())) {
                    Map<String, Map<Long, URL>> services = this.registryCache.get(category);
                    if (services != null) {
                        String group = url.getParameter("group");
                        String version = url.getParameter("version");

                        if (!"*".equals(group) && !"*".equals(version)) {
                            services.remove(url.getServiceKey());
                            continue;
                        }

                        for (Entry<String, Map<Long, URL>> serviceEntry : services.entrySet()) {
                            String service = serviceEntry.getKey();
                            if (getInterface(service).equals(url.getServiceInterface()) &&
                                    ("*".equals(group) || StringUtils.isEquals(group, getGroup(service))) &&
                                    ("*".equals(version) || StringUtils.isEquals(version, getVersion(service)))) {

                                services.remove(service);
                            }
                        }
                    }
                    continue;
                }

                Map<String, Map<Long, URL>> services = categories.computeIfAbsent(category, k -> new HashMap<>());
                String service = url.getServiceKey();

                Map<Long, URL> ids = services.computeIfAbsent(service, k -> new HashMap<>());

                ids.put(ID.incrementAndGet(), url);
            }

            for (Map.Entry<String, Map<String, Map<Long, URL>>> categoryEntry : categories.entrySet()) {
                String category = categoryEntry.getKey();
                Map<String, Map<Long, URL>> services = this.registryCache.get(category);

                if (services == null) {
                    services = new ConcurrentHashMap<>();
                    this.registryCache.put(category, services);
                }
                services.putAll(categoryEntry.getValue());
            }
        }
    }


    @Override
    public void destroy()  {
        if(this.registryService !=null) {
            this.registryService.unsubscribe(SUBSCRIBE, this);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if(this.registryService != null){
            this.registryService.subscribe(SUBSCRIBE, this);
        }
    }

    private List<URL> findAddressesByService(String service) {
        List<URL> ret = new ArrayList<>();
        Map<String, Map<Long, URL>> providerUrls = this.registryCache.get("providers");
        if (null != providerUrls && !providerUrls.isEmpty() && null != providerUrls.get(service) && !providerUrls.get(service).isEmpty()) {

            Set<Map.Entry<Long, URL>> set = providerUrls.get(service).entrySet();
            set.forEach( e ->  ret.add(e.getValue()));
            return ret;
        } else {
            return ret;
        }
    }

    /* ervicekye =  $group_$serviceName_$version */
    private String getServiceKey(String service,String version, String group){
        StringBuilder sb = new StringBuilder();
        if(group != null && group.length() > 0){
            sb.append(group).append("/");
        }

        sb.append(service);

        if(StringUtils.isNotEmpty(version)){
            sb.append(":").append(version);
        }

        return sb.toString();
    }

    private String getVersion(String serviceKey) {
        if (serviceKey != null && serviceKey.length() > 0) {
            int i = serviceKey.lastIndexOf(58);
            if (i >= 0) {
                return serviceKey.substring(i + 1);
            }
        }

        return null;
    }

    private String getGroup(String serviceKey) {
        if (serviceKey != null && serviceKey.length() > 0) {
            int i = serviceKey.indexOf(47);
            if (i >= 0) {
                return serviceKey.substring(0, i);
            }
        }

        return null;
    }

    private String getInterface(String serviceKey){
        if (serviceKey != null && serviceKey.length() > 0 ){
            int i = serviceKey.indexOf(47);
            if(i >= 0){
                serviceKey = serviceKey.substring( i+1 );
            }

            i = serviceKey.lastIndexOf(58);
            if( i >= 0 ){
                serviceKey = serviceKey.substring(0,i);
            }
        }
        return serviceKey;
    }
}

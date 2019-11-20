package com.jokls.jok.rpc.def.config.spring;

import com.jokls.jok.common.util.ConfigUtils;
import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.rpc.annotation.CloudComponent;
import com.jokls.jok.rpc.annotation.CloudReference;
import com.jokls.jok.rpc.annotation.CloudService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.config.AbstractConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.ServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationBean extends AbstractConfig implements DisposableBean, BeanPostProcessor, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationBean.class);
    public static final String COM_ALIBABA_DUBBO_COMMON = "com.alibaba.dubbo.common";
    public static final String ORG_APACHE_DUBBO_COMMON = "org.apache.dubbo.common";
    public static final String PATTERN = "\\$\\{.+\\}";
    public static final String APP_REFERENCE = "app.reference.";
    public static final String CHARSET = ".charset";
    private final Set<ServiceConfig<?>> serviceConfigs = new ConcurrentHashSet<>();
    private final ConcurrentHashMap<String, ReferenceConfig<?>> referenceConfigs = new ConcurrentHashMap<>();
    private static ApplicationContext applicationContext;
    private static String transCharset;
    private boolean singleMode;
    private String[] consumerFilters;
    private String[] providerFilters;
    private static String DEFAULT_TRANS_CHARSET = "UTF-8";

    public AnnotationBean(String[] consumerFilters, String[] providerFilters, String protocolCharset, boolean singleMode) {
        transCharset = protocolCharset;
        this.singleMode = singleMode;
        this.consumerFilters = consumerFilters;
        this.providerFilters = providerFilters;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.serviceConfigs.stream().filter( s ->  Boolean.TRUE.equals(s.isRegister())).forEach(ServiceConfig::export);
    }


    public static String getTransCharset() {
        if (transCharset == null) {
            transCharset = DEFAULT_TRANS_CHARSET;
        }

        return transCharset;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        if(!clazz.getName().startsWith(ORG_APACHE_DUBBO_COMMON) && !clazz.getName().startsWith(COM_ALIBABA_DUBBO_COMMON)){
            if(this.isProxyBean(bean)){
                clazz = AopUtils.getTargetClass(bean);
            }

            Class<?>[] interfaces = clazz.getInterfaces();
            if(interfaces != null){
                for(Class intf : interfaces){
                    CloudService cloudService = AnnotatedElementUtils.findMergedAnnotation(intf, CloudService.class);
                    if (cloudService != null && (!this.singleMode || !cloudService.singleMode())) {
                        this.export(cloudService, clazz, bean);
                        break;
                    }
                }
            }
        }

        return bean;
    }

    private void export(CloudService service, Class<?> clazz, Object bean) throws BeansException {
        if(service != null){
            StringBuilder sb = new StringBuilder();
            if(this.providerFilters != null){
                sb.append(String.join( ",",this.providerFilters));
            }

            CloudComponent cloudComponent = AnnotatedElementUtils.findMergedAnnotation(clazz,CloudComponent.class);
            Class<?>[] filters = cloudComponent.filters();
            String filterstr = Arrays.stream(filters).map(Class::getName).collect(Collectors.joining(","));
            sb.append(filterstr);

            ServiceBean serviceConfig = new ServiceBean(service);
            serviceConfig.setFilter(sb.toString());
//            serviceConfig.setOpenApi(service.openApi());
            serviceConfig.setValidation(service.validation()? "true":null);

            if(clazz.getInterfaces().length <=0 ){
                logger.error("Failed to export remote service class {} ",clazz.getName());
                throw new IllegalStateException("Failed to export remote service class " + clazz.getName() + ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
            }

            serviceConfig.setInterface(clazz.getInterfaces()[0]);
            if(StringUtils.isEmpty(service.group())){
                serviceConfig.setGroup(ConfigUtils.getAppGroup());
            }

            if(StringUtils.isEmpty(service.version())){
                serviceConfig.setVersion(ConfigUtils.getAppVersion());
            }

            serviceConfig.setRef(bean);

            if(applicationContext != null){
                serviceConfig.setApplicationContext(applicationContext);

                try{
                    serviceConfig.afterPropertiesSet();
                } catch (Exception e) {
                    logger.error("serviceConfig.afterPropertySet error " ,e);
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }


            this.serviceConfigs.add(serviceConfig);
            if( !Boolean.TRUE.equals(serviceConfig.isRegister())){
                serviceConfig.export();
            }
//            if (!serviceConfig.isRegistryAfterSpring()) {
//                serviceConfig.export();
//            }


        }
    }


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if(!clazz.getName().startsWith(COM_ALIBABA_DUBBO_COMMON) && !clazz.getName().startsWith(ORG_APACHE_DUBBO_COMMON)){
            if(this.isProxyBean(bean)){
                clazz = AopUtils.getTargetClass(bean);
            }

            Field[] fields = clazz.getDeclaredFields();
            Arrays.stream(fields).forEach( f -> {
                try {
                    f.setAccessible(true);

                    CloudReference cloudReference = AnnotatedElementUtils.findMergedAnnotation(f, CloudReference.class);
                    CloudService cloudService = AnnotatedElementUtils.findMergedAnnotation(f.getType(), CloudService.class);

                    if (cloudReference != null && cloudService != null) {
                        Object value = this.refer(cloudReference, f.getType());
                        if (value != null) {
                            f.set(bean, value);
                        }
                    }
                }catch (IllegalAccessException e) {
                    throw new BeanInitializationException("Failed to init remote service reference at filed " + f.getName() + " in class " + bean.getClass().getName(), e);
                }
            });

        }

        return bean;
    }

    private Object refer(CloudReference reference, Class<?> refClass) {
        if(refClass.isInterface()){
            String interfaceName = refClass.getName();
            String group  = reference.group();

            if(!StringUtils.isEmpty(group) && group.trim().matches(PATTERN)){
                group = ConfigUtils.get(group.trim().substring(2, group.length() -1 ));
            }else if (StringUtils.isEmpty(group)){
                group = ConfigUtils.getAppGroup();
            }

            String version = reference.version();
            if(!StringUtils.isEmpty(version) && version.trim().matches(PATTERN)) {
                version = ConfigUtils.get(version.trim().substring(2, version.length() -1));
            }else{
                version = ConfigUtils.getAppVersion();
            }

            String charset = reference.charset();
            if(!StringUtils.isEmpty(charset) && charset.trim().matches(PATTERN)) {
                charset = ConfigUtils.get(charset.trim()).substring(2, charset.length() -1 );
            }else if(StringUtils.isEmpty(charset) && !StringUtils.isEmpty(reference.service())){
                charset = ConfigUtils.get(APP_REFERENCE + reference.service() + CHARSET);
            }

            String targetUrl = reference.targetUrl();
            if(!StringUtils.isEmpty(targetUrl) && targetUrl.trim().matches(PATTERN)){
                targetUrl = ConfigUtils.get(targetUrl.trim().substring(2, targetUrl.length()-1));
            } else {
                targetUrl = ConfigUtils.get(APP_REFERENCE + reference.service() + ".url");
            }

            String key = group + "/" + interfaceName + ":" + version + "/callType_" + reference.callType();
            if(!StringUtils.isEmpty(targetUrl)){
                key = key + "/url_" + targetUrl;
            }

            if(!StringUtils.isEmpty(reference.service())){
                key = key + "/service_" + reference.service();
            }

            ReferenceConfig<?> referenceConfig = this.referenceConfigs.get(key);
            if (referenceConfig == null){
                StringBuilder sb = new StringBuilder();

                if(this.consumerFilters != null){
                    sb.append(String.join(",",this.consumerFilters)).append(",");
                }

                Class<?>[] filters = reference.filters();
                String filtersStr = Arrays.stream(filters).map(Class::getName).collect(Collectors.joining(","));
                sb.append(filtersStr);

                //todo: need change
                referenceConfig = new ReferenceBean();

//                referenceConfig.setReffilters(sb.toString());
//                referenceConfig.setRefKey(key);
                referenceConfig.setInterface(refClass);
                referenceConfig.setGroup(group);
                referenceConfig.setVersion(version);

//                if (!"future".equals(reference.callType()) && !"callback".equals(reference.callType())) {
//                    if ("oneway".equals(reference.callType())) {
//                        referenceConfig.setReturn(false);
//                    }
//                } else {
//                    referenceConfig.setAsync(true);
//                    if ("callback".equals(reference.callType())) {
//                        referenceConfig.setCallbackclass(reference.callbackClass().getName());
//                    }
//                }

                if (!StringUtils.isEmpty(targetUrl)) {
                    referenceConfig.setUrl(targetUrl);
                } else {
                    referenceConfig.setUrl(null);
                }

//                referenceConfig.setCharset(charset);
//                referenceConfig.setService(reference.service());
                if (applicationContext != null) {
                    ((ReferenceBean)referenceConfig).setApplicationContext(applicationContext);

                    try {
                        ((ReferenceBean)referenceConfig).afterPropertiesSet();
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
                this.referenceConfigs.putIfAbsent(key, referenceConfig);
                referenceConfig = this.referenceConfigs.get(key);
            }
            return referenceConfig.get();

        }else{
            throw new IllegalStateException("The @CloudReference undefined interfaceClass or interfaceName, and the property type " + refClass.getName() + " is not a interface.");
        }

    }

    @Override
    public void destroy(){
        this.serviceConfigs.forEach(ServiceConfig::unexport);
        this.referenceConfigs.values().forEach(ReferenceConfig::destroy);
    }

    public Object ref(URL url) {
        return ((ReferenceConfig)this.referenceConfigs.get(url.getParameter("refkey"))).get();
    }

    private boolean isProxyBean(Object bean) {
        return AopUtils.isAopProxy(bean);
    }
}

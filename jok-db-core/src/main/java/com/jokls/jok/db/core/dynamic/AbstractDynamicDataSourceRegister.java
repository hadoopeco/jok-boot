package com.jokls.jok.db.core.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.jokls.jok.common.code.ErrorCode;
import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.db.core.exception.BaseDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:02
 */
public abstract class AbstractDynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractDynamicDataSourceRegister.class);
    protected static final String DATASOURCE_TYPE_JNDI_WEBLOGIC ="weblogic-jndi";
    protected static final String DATASOURCE_TYPE_JNDI_TOMCAT = "tomcat-jndi";
    protected static final String DATASOURCE_TYPE_DBP = "dbp";
    protected static final String DATASOURCE_TYPE_ZDAL = "zdal";
    protected DataSource defaultDataSource;
    protected Map<String, DataSource> customDataSources = new HashMap<>();
    protected static Map<String, String> dsIds = new HashMap<>();
    public static final Pattern KEY_SPLIT_PATTERN = Pattern.compile("\\s*[.]+\\s*");
    public static final Pattern BIZKEY_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

    public static String getDsId(String key) {
        return dsIds.get(key);
    }

    public void setEnvironment(Environment environment){
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "jok.datasource.");
        Map<String, Object> map = propertyResolver.getSubProperties(null);

        List<String> dsPrefixs = new ArrayList<>();
        String name;
        for(Map.Entry<String, Object> entry : map.entrySet()){
            String key  = entry.getKey();
            if(!StringUtils.isEmpty(key)){
                name = KEY_SPLIT_PATTERN.split(key)[0];
                if (!dsPrefixs.contains(name)) {
                    dsPrefixs.add(name);
                }
            }
        }

        Iterator<String> iterator = dsPrefixs.iterator();

        while(true) {
            String bizkeys;
            String dsPrefix;
            String loading;
            do {
                Map dsMap;
                do {
                    if (!iterator.hasNext()) {
                        return;
                    }

                    dsPrefix = iterator.next();
                    dsMap = propertyResolver.getSubProperties(dsPrefix + ".");
                    loading = (String)dsMap.get("loading");
                } while("false".equals(loading));

                DataSource ds = this.buildDataSource(dsPrefix, dsMap, environment);
                if ("default".equals(dsPrefix)) {
                    this.defaultDataSource = ds;
                } else {
                    this.customDataSources.put(dsPrefix, ds);
                }

                bizkeys = (String)dsMap.get("bizkeys");
            } while(StringUtils.isEmpty(bizkeys));

            String[] keyArray = BIZKEY_SPLIT_PATTERN.split(bizkeys);

            for(String key : keyArray) {
                dsIds.put(key, dsPrefix);
            }
        }

    }

    protected DruidDataSource getDruidDataSource(String dsPrefix, Map<String, Object> dsMap, Environment env, DruidDataSource druidDs, String type) throws  SQLException {
        Object driverClassName = dsMap.get("driverClassName");
        if (driverClassName == null) {
            driverClassName = dsMap.get("driver-class-name");
        }

        String url = (String)dsMap.get("url");

        if (StringUtils.isEmpty(url)) {
            throw new BaseDBException(ErrorCode.DB.DS_ACCESS_ERROR_URL_NULL, "数据源[" + dsPrefix + "]url未配置!");
        } else {
            String username = (String)dsMap.get("username");
            if (StringUtils.isEmpty(username)) {
                throw new BaseDBException(ErrorCode.DB.DS_ACCESS_ERROR_USERNAME_NULL, "数据源[" + dsPrefix + "]username未配置!");
            } else {
                String password = (String)dsMap.get("password");
                if (StringUtils.isEmpty(password)) {
                    throw new BaseDBException(ErrorCode.DB.DS_ACCESS_ERROR_PASSWORD_NULL, "数据源[" + dsPrefix + "]password未配置!");
                } else {
                    druidDs.setInitialSize(env.getProperty("jok.druid.initialSize", Integer.class, 1));
                    druidDs.setMinIdle(env.getProperty("jok.druid.minIdle", Integer.class, 1));
                    druidDs.setMaxActive(env.getProperty("jok.druid.maxActive", Integer.class, 20));
                    druidDs.setMaxWait(env.getProperty("jok.druid.maxWait", Long.class, 60000L));
                    druidDs.setTimeBetweenEvictionRunsMillis(env.getProperty("jok.druid.timeBetweenEvictionRunsMillis", Long.class, 60000L));
                    druidDs.setMinEvictableIdleTimeMillis(env.getProperty("jok.druid.minEvictableIdleTimeMillis", Long.class, 300000L));
                    String validationQuery = env.getProperty("druid.validationQuery", String.class);
                    if (!StringUtils.isEmpty(validationQuery)) {
                        druidDs.setValidationQuery(validationQuery);
                    } else {
                        druidDs.setValidationQuery("select 1 from dual");
                    }

                    druidDs.setTestWhileIdle(env.getProperty("jok.druid.testWhileIdle", Boolean.class, true));
                    druidDs.setTestOnBorrow(env.getProperty("jok.druid.testOnBorrow", Boolean.class, false));
                    druidDs.setTestOnReturn(env.getProperty("jok.druid.testOnReturn", Boolean.class, false));
                    druidDs.setPoolPreparedStatements(env.getProperty("jok.druid.poolPreparedStatements", Boolean.class, true));
                    druidDs.setMaxPoolPreparedStatementPerConnectionSize((Integer)env.getProperty("jok.druid.maxPoolPreparedStatementPerConnectionSize", Integer.class, 20));
                    druidDs.setRemoveAbandoned(env.getProperty("jok.druid.removeAbandoned", Boolean.class, true));
                    druidDs.setRemoveAbandonedTimeout(env.getProperty("jok.druid.removeAbandonedTimeout", Integer.class, 180));
                    druidDs.setLogAbandoned(env.getProperty("jok.druid.logAbandoned", Boolean.class, true));
                    druidDs.setUrl(url);
                    druidDs.setUsername(username);
                    druidDs.setDriverClassName(driverClassName.toString());
                    druidDs.setPassword(password);
                    druidDs.init();
                    return druidDs;
                }
            }
        }
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("default", this.defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("default");
        targetDataSources.putAll(this.customDataSources);

        DynamicDataSourceContextHolder.dataSourceIds.addAll(this.customDataSources.keySet());

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        beanDefinition.setPrimary(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", this.defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        mpv.addPropertyValue("dataSourcesMap", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
    }

    public abstract DataSource buildDataSource(String prefix, Map<String, Object> params, Environment env);
}

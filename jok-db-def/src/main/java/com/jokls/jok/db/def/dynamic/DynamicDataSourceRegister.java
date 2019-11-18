package com.jokls.jok.db.def.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.jokls.jok.db.core.dynamic.AbstractDynamicDataSourceRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/7/9 10:50
 */
public class DynamicDataSourceRegister extends AbstractDynamicDataSourceRegister {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);


    public DynamicDataSourceRegister() {
    }


    /** create datasource registe to the springContext
     * @param prefix
     * @param dsMap
     * @param env
     * @return
     */
    @Override
    public DataSource buildDataSource(String prefix, Map<String, Object> dsMap, Environment env) {
        String type = (String)dsMap.get("type");
        try{
            return this.getDruidDataSource(prefix, dsMap, env, new DruidDataSource(), type);
        } catch (Exception e) {
            logger.error("数据源配置异常！", e);
            return null;
        }
    }
}

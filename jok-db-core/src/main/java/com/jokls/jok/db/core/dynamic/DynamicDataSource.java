package com.jokls.jok.db.core.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 12:00
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private Map<Object, Object> dataSourcesMap;

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
    public void setDataSourcesMap(Map<Object,Object> dataSourcesMap){this.dataSourcesMap = dataSourcesMap ;}

    public Map<Object, Object> getDataSourcesMap(){
        return this.dataSourcesMap;
    }
    public DataSource getCurrentDataSource() {
        return (DataSource)this.dataSourcesMap.get(DynamicDataSourceContextHolder.getDataSourceType() != null ? DynamicDataSourceContextHolder.getDataSourceType() : "default");
    }

    public DataSource getDataSource(String dsId) {
        return (DataSource)this.dataSourcesMap.get(dsId);
    }



}

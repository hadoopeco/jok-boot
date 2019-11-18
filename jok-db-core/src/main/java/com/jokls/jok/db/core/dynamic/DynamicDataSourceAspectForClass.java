package com.jokls.jok.db.core.dynamic;

import com.jokls.jok.db.core.annotation.TargetDataSource;
import com.jokls.jok.db.core.location.DsLocationFactory;
import com.jokls.jok.db.core.location.DsLocationable;
import com.jokls.jok.db.core.exception.BaseDBException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 14:20
 */

@Aspect
@Order(-1)
public class DynamicDataSourceAspectForClass {

    @Before("@within(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource){
        String dsId = targetDataSource.value();
        Class<?> clazz = targetDataSource.location();

        if(StringUtils.isEmpty(dsId) && !Void.class.equals(clazz)){
            DsLocationable locationable = DsLocationFactory.getInstance().getDSLocationable(clazz.getName());
            String key = locationable.getLocationkey(point.getArgs());
            if(!StringUtils.isEmpty(key)){
                dsId = AbstractDynamicDataSourceRegister.getDsId(key);
            }
        }

        if(!StringUtils.isEmpty(dsId) && !"default".equals(dsId) && !DynamicDataSourceContextHolder.containsDataSource(dsId)){
            throw new BaseDBException(2001, "数据源[" + dsId + "]不存在!");
        } else {
            DynamicDataSourceContextHolder.setDataSourceType(dsId);
        }
    }

    @After("@within(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}

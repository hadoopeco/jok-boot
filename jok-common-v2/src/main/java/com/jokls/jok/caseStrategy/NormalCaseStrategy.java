package com.jokls.jok.caseStrategy;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:10
 */
public class NormalCaseStrategy extends BaseCaseStrategy {
    @Override
    public String getPropertyName(String fieldName) {
        return fieldName.toLowerCase();
    }

    @Override
    public String getFieldName(String propertyName) {
        return propertyName;
    }
}

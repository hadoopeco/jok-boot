package com.jokls.jok.event;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasets;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/13 17:14
 */
public interface IEvent {

    void setServiceId(String serviceId);

    void setServiceAlias(String serviceAlias);

    String getServiceId();

    String getServiceAlias();

    void setEventType(int type);

    int getEventType();

    boolean isTimestampOn();

    void markTimestamp(boolean flag);

    void addTimestamp(long time, String timestampInfo);

    String[] getTimestamp();

    int changeToresponse();

    IDatasets getEventDatas();

    void putEventData(IDataset dataset);

    void putEventDatas(IDatasets datasets);

    void setReturnCode(int returnCode);

    int getReturnCode();

    void setErrorCode(String errorNo, String errorInfo);

    String getErrorNo();

    String getErrorInfo();

    int getAttributeCount();

    String getAttributeName(int index);

    char getAttributeType(String name);

    boolean hasAttribute(String tagName);

    void setIntegerAttributeValue(String tagName, long value);

    long getIntegerAttributeValue(String tagName);

    void setStringAttributeValue(String tagName, String value);

    String getStringAttributeValue(String tagName);

    void setStringArrayAttributeValue(String tagName, String[] value);

    String[] getStringArrayAttributeValue(String tagName);

    void setByteArrayAttributeValue(String tagName, byte[] value);

    byte[] getByteArrayAttributeValue(String tagName);

    void setAttributeValue(String tagName, Object value);

    <T> T getAttributeValue(String tagName);

    void removeAttribute(String name);
}

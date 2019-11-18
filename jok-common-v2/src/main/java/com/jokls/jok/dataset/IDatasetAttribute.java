package com.jokls.jok.dataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 13:45
 */
public interface IDatasetAttribute {
    void copyFrom(IDatasetAttribute attribute);

    int getDefInt();

    void setDefInt(int defInt);

    long getDefLong();

    void setDefLong(long def);

    double getDefDouble();

    void setDefDouble(double def);

    String getDefString();

    void setDefString(String def);

    byte[] getDefBytes();

    void setDefBytes(byte[] def);

    String[] getDefStrings();

    void setDefStrings(String[] def);

    int getMaxClobLength();

    void setMaxClobLength(int maxClobLength);

    int getMaxBlobLength();

    void setMaxBlobLength(int maxBlobLength);

    String getDateFormat();

    void setDateFormat(String dateFormat);

    boolean isBoolAsString();

    void setBoolAsString(boolean boolAsString);

    /** @deprecated */
    @Deprecated
    int getWorkMode();
}

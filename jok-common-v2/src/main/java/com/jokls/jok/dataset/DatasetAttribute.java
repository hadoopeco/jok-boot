package com.jokls.jok.dataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 13:46
 */
public class DatasetAttribute implements IDatasetAttribute {
    protected int defInt = 0;
    protected long defLong = 0L;
    protected double defDouble = 0.0D;
    protected String defString  = "";
    protected byte[] defBytes = new byte[0];
    protected String[] defStrings = new String[0];
    protected boolean boolAsString = true;
    protected int maxClobLength = 0;
    protected int maxBlobLength = 0;
    protected String dateFormat = "yyyy-MM-dd' 'HH:mm:ss";
    protected int workmode = 1;

    @Override
    public void copyFrom(IDatasetAttribute attr) {
        this.defInt = attr.getDefInt();
        this.defLong = attr.getDefLong();
        this.defDouble = attr.getDefDouble();
        this.setDefString(attr.getDefString());
        this.setDefBytes(attr.getDefBytes());
        this.setDefStrings(attr.getDefStrings());
        this.boolAsString = attr.isBoolAsString();
        this.maxClobLength = attr.getMaxClobLength();
        this.maxBlobLength = attr.getMaxBlobLength();
        this.setDateFormat(attr.getDateFormat());

    }

    public int getDefInt() {
        return this.defInt;
    }

    public void setDefInt(int defInt) {
        this.defInt = defInt;
    }

    public long getDefLong() {
        return this.defLong;
    }

    public void setDefLong(long defLong) {
        this.defLong = defLong;
    }

    public double getDefDouble() {
        return this.defDouble;
    }

    public void setDefDouble(double defDouble) {
        this.defDouble = defDouble;
    }

    public String getDefString() {
        return this.defString;
    }

    public void setDefString(String defString) {
        if (defString == null) {
            defString = "";
        }

        this.defString = defString;
    }

    public byte[] getDefBytes() {
        return this.defBytes;
    }

    public void setDefBytes(byte[] defBytes) {
        if (defBytes == null) {
            defBytes = new byte[0];
        }

        this.defBytes = defBytes;
    }

    public String[] getDefStrings() {
        return this.defStrings;
    }

    public void setDefStrings(String[] defStrings) {
        if (defStrings == null) {
            defStrings = new String[0];
        }

        this.defStrings = defStrings;
    }

    public boolean isBoolAsString() {
        return this.boolAsString;
    }

    public void setBoolAsString(boolean boolAsString) {
        this.boolAsString = boolAsString;
    }

    public int getMaxClobLength() {
        return this.maxClobLength;
    }

    public void setMaxClobLength(int maxClobLength) {
        this.maxClobLength = maxClobLength;
    }

    public int getMaxBlobLength() {
        return this.maxBlobLength;
    }

    public void setMaxBlobLength(int maxBlobLength) {
        this.maxBlobLength = maxBlobLength;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }

    }

    public int getWorkMode() {
        return this.workmode;
    }
}

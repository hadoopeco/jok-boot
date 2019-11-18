package com.jokls.jok.event.field;

import com.jokls.jok.dataset.DatasetAttribute;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasetAttribute;
import com.jokls.jok.exception.DatasetRuntimeException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:24
 */
public class FieldValue {
    private final char DEFAULT_TYPE = 'T';
    private final char BOOLEAN_TYPE = 'B';
    private char type = 'N' ;
    private Object value;
    private IDatasetAttribute dsa;



    public FieldValue() {
        this.value = null;
        this.type = 'T';
    }

    protected FieldValue(IDataset value) {
        this.type = 'P';
        this.value = value;
    }

    public FieldValue(FieldValue other){
        this.type = other.type;
        this.value = other.value;
        this.dsa = other.dsa;
    }

    protected FieldValue(byte[] value, IDatasetAttribute dsa){
        if(value == null){
            value = new byte[0];
        }

        fillValue('R', value, dsa);
    }

    protected FieldValue(String value, IDatasetAttribute dsa){
        if(value == null){
            value = "";
        }

        fillValue('S', value, dsa);
    }

    protected FieldValue(String[] value, IDatasetAttribute dsa){
        if(value == null ){
            value = new String[0];
        }

        if(dsa == null){
            dsa = new DatasetAttribute();
        }

        fillValue('A', value, dsa);
    }

    protected FieldValue(int value, IDatasetAttribute dsa) {
        fillValue('I', value, dsa);
    }

    protected FieldValue(long value, IDatasetAttribute dsa){
        fillValue('L', value, dsa);
    }

    protected FieldValue(double value, IDatasetAttribute dsa){
        fillValue('D', value, dsa);
    }

    protected FieldValue(boolean value, IDatasetAttribute dsa){
        fillValue('B', value, dsa);
    }
    private void fillValue(char T, Object value, IDatasetAttribute dsa){
        if( dsa == null){
            dsa = new DatasetAttribute();
        }
        this.type = T ;
        this.value = value;
        this.dsa = dsa;
    }

    public byte[] getByteArray() {
        return this.getByteArray(this.dsa.getDefBytes());
    }

    public byte[] getByteArray(byte[] def) {
        if (this.type != 'R' && this.type != 'P') {
            if (this.type != 'S' && this.type != 'D' && this.type != 'I' && this.type != 'L') {
                if (this.useDefaultValue()) {
                    return def;
                } else {
                    throw new DatasetRuntimeException("70", this.shortData(), "byte[]");
                }
            } else {
                return this.value.toString().getBytes();
            }
        } else {
            return (byte[])this.value;
        }
    }



    public String getString() {
        return this.getString(this.dsa.getDefString());
    }

    public String getString(String def) {
        if (this.type == 'S') {
            return this.value.toString();
        } else if (this.type == 'B') {
            return this.value.toString();
        } else if (this.type == 'R') {
            return new String((byte[])((byte[])this.value));
        } else if (this.type != 'D' && this.type != 'I' && this.type != 'L') {
            if (this.useDefaultValue()) {
                return def;
            } else {
                throw new DatasetRuntimeException("70", this.shortData(), "string");
            }
        } else {
            return this.value.toString();
        }
    }

    public String[] getStringArray() {
        return this.getStringArray(this.dsa.getDefStrings());
    }

    public String[] getStringArray(String[] def) {
        if (this.type == 'A') {
            return (String[])((String[])this.value);
        } else if (this.type == 'R') {
            return new String[]{new String((byte[])((byte[])this.value))};
        } else if (this.type != 'D' && this.type != 'I' && this.type != 'L' && this.type != 'S') {
            if (this.useDefaultValue()) {
                return def;
            } else {
                throw new DatasetRuntimeException("70", this.shortData(), "string");
            }
        } else {
            return new String[]{this.value.toString()};
        }
    }

    public int getInt() {
        return this.getInt(this.dsa.getDefInt());
    }

    public int getInt(int def) {
        if (this.useDefaultValue()) {
            return def;
        } else if (this.type == 'I') {
            return (Integer)this.value;
        } else if (this.type == 'D') {
            try {
                Double d = (Double)this.value;
                return d.intValue();
            } catch (Exception e) {
                throw new DatasetRuntimeException("70", this.shortData(), "int");
            }
        } else if (this.type == 'B') {
            boolean v = (Boolean)this.value;
            return v ? 1 : 0;
        } else if (this.type == 'R') {
            String s = new String((byte[])this.value);

            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                throw new DatasetRuntimeException("70", this.shortData(), "int");
            }
        } else {
            try {
                return Integer.parseInt(this.value.toString());
            } catch (Exception e) {
                Double d = this.attempt2Transform();
                if (d == null) {
                    throw new DatasetRuntimeException("70", this.shortData(), "int");
                } else {
                    return d.intValue();
                }
            }
        }
    }

    public long getLong() {
        return this.getLong(this.dsa.getDefLong());
    }

    public long getLong(long def) {
        if (this.useDefaultValue()) {
            return def;
        } else if (this.type == 'L') {
            return (Long)this.value;
        } else if (this.type == 'D') {
            try {
                Double d = (Double)this.value;
                return d.longValue();
            } catch (Exception e) {
                throw new DatasetRuntimeException("70", this.shortData(), "long");
            }
        } else if (this.type == 'B') {
            boolean v = (Boolean)this.value;
            return v ? 1L : 0L;
        } else if (this.type == 'R') {
            String s = new String((byte[])this.value);

            try {
                return Long.parseLong(s);
            } catch (Exception e) {
                throw new DatasetRuntimeException("70", this.shortData(), "long");
            }
        } else {
            try {
                return Long.parseLong(this.value.toString());
            } catch (Exception e) {
                Double d = this.attempt2Transform();
                if (d == null) {
                    throw new DatasetRuntimeException("70", this.shortData(), "long");
                } else {
                    return d.longValue();
                }
            }
        }
    }

    public double getDouble() {
        return this.getDouble(this.dsa.getDefDouble());
    }

    public double getDouble(double def) {
        if (this.useDefaultValue()) {
            return def;
        } else if (this.type == 'D') {
            return (Double)this.value;
        } else {
            try {
                String s;
                if (this.type == 'R') {
                    s = new String((byte[])this.value);
                } else {
                    s = this.value.toString();
                }

                return Double.parseDouble(s);
            } catch (Exception e) {
                throw new DatasetRuntimeException("70", this.shortData(), "double");
            }
        }
    }

    public char getType() {
        char type = this.type;
        if (type == 'B') {
            type = 'S';
        }

        return type;
    }

    public Object getValue() {
        return this.value;
    }


    protected Double attempt2Transform() {
        try {
            Double d = Double.parseDouble(this.value.toString());
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private String shortData() {
        StringBuffer sb = new StringBuffer();
        if (this.type == 'R') {
            sb.append(new String((byte[])this.value));
        } else if (this.type == 'A') {
            sb.append("String[] -- ");
            String[] tmp = (String[])this.value;

            for(String str : tmp){
                sb.append(str);
                sb.append("_@I@_");
            }

        } else {
            sb.append(this.value);
        }

        String print = sb.toString();
        if (print.length() > 100) {
            String t = print.substring(0, 100) + " ...";
            return t;
        } else {
            return print;
        }
    }

    public boolean useDefaultValue() {
        return 'T' == this.type;
    }

    public String toString() {
        String data = "can't display!";

        try {
            data = this.shortData();
        } catch (Exception ignored) {
        }

        return "[" + data + "," + this.type + "]";
    }

    public FieldValue clone() {
        try {
            return (FieldValue)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

}

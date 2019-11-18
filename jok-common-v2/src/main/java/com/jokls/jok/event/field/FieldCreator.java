package com.jokls.jok.event.field;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasetAttribute;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 14:17
 */
public class FieldCreator {
    private static Map<Integer, Integer> typeMap = new HashMap();

    private IDatasetAttribute dsa ;

    public FieldCreator(IDatasetAttribute dsa) {
        this.dsa = dsa;
    }

    public static FieldCreator getNewInstance(IDatasetAttribute attr) {
        return new FieldCreator(attr);
    }

    public Field getField(String name, int type){
        Integer baseType =  typeMap.get(type);
        char interType = baseType == null ? 'S': (char)baseType.intValue();
        return new Field(name, interType);
    }

    public FieldValue getDefaultValue(int type){
        Integer baseType = typeMap.get(type);
        char interType = baseType == null ? 'S':(char) baseType.intValue();

        switch (interType){

            case 'D':
                return new FieldValue(this.dsa.getDefDouble(), this.dsa);
            case 'I':
                return new FieldValue(this.dsa.getDefInt(), this.dsa);
            case 'L':
                return new FieldValue(this.dsa.getDefLong(), this.dsa);
            case 'R':
                return new FieldValue(this.dsa.getDefBytes(), this.dsa);
            case 'S':
                return new FieldValue(this.dsa.getDefString(), this.dsa);
            case 'A':
            case 'B':
            case 'C':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'J':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            default:
                return new FieldValue(this.dsa.getDefString(), this.dsa);

        }
     }

    public FieldValue getFieldValue(byte[] value) {
        if (value == null) {
            value = this.dsa.getDefBytes();
        }

        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(int value) {
        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(long value) {
        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(String value) {
        if (value == null) {
            value = this.dsa.getDefString();
        }

        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(String[] value) {
        if (value == null) {
            value = this.dsa.getDefStrings();
        }

        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(double value) {
        return new FieldValue(value, this.dsa);
    }

    public FieldValue getFieldValue(Object value) {
        if (value == null) {
            return new FieldValue();
        } else if (value instanceof String) {
            return new FieldValue(value.toString(), this.dsa);
        } else if (value instanceof Number) {
            if (value instanceof BigDecimal) {
                BigDecimal tmp = (BigDecimal)value;
                return tmp.scale() <= 0 ? new FieldValue(tmp.longValue(), this.dsa) : new FieldValue(tmp.doubleValue(), this.dsa);
            } else if (!(value instanceof BigDecimal) && !(value instanceof Float) && !(value instanceof Double)) {
                return value instanceof Long ? new FieldValue((Long)value, this.dsa) : new FieldValue(Integer.valueOf(value.toString()), this.dsa);
            } else {
                return new FieldValue(Double.valueOf(value.toString()), this.dsa);
            }
        } else if (value instanceof byte[]) {
            return new FieldValue((byte[])value, this.dsa);
        } else if (value instanceof String[]) {
            return new FieldValue((String[])value, this.dsa);
        } else if (value instanceof Character) {
            return new FieldValue(value.toString(), this.dsa);
        } else {
            SimpleDateFormat formatter;
            if (value instanceof Date) {
                formatter = new SimpleDateFormat(this.dsa.getDateFormat());
                return new FieldValue(formatter.format(value), this.dsa);
            } else if (value instanceof Calendar) {
                formatter = new SimpleDateFormat(this.dsa.getDateFormat());
                return new FieldValue(formatter.format(((Calendar)value).getTime()), this.dsa);
            } else if (value instanceof Locale) {
                return new FieldValue(value.toString(), this.dsa);
            } else if (value instanceof Enum) {
                return new FieldValue(((Enum)value).name(), this.dsa);
            } else if (value instanceof Boolean) {
                return new FieldValue((Boolean)value, this.dsa);
            } else if (value instanceof Clob) {
                String clobStr = "";
                Clob clob = (Clob)value;

                try {
                    if (this.dsa.getMaxClobLength() != 0 && (long)this.dsa.getMaxClobLength() < clob.length()) {
                        clobStr = clob.getSubString(1L, this.dsa.getMaxClobLength()) + "...";
                    } else {
                        clobStr = clob.getSubString(1L, (int)clob.length());
                    }
                }catch (SQLException e) {
                    clobStr = "";
                }

                return new FieldValue(clobStr, this.dsa);
            } else if (value instanceof Blob) {
                try {
                    Blob blob = (Blob)value;
                    return new FieldValue(blob.getBytes(1L, (int)blob.length()), this.dsa);
                } catch (SQLException e) {
                    return new FieldValue(new byte[0], this.dsa);
                }
            } else {
                if (value instanceof Serializable) {
                    try {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        os.write("#jresobj#".getBytes());
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(value);
                        return new FieldValue(os.toByteArray(), this.dsa);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (value instanceof IDataset) {
                    return new FieldValue((IDataset)value);
                }

                return new FieldValue(value.toString(), this.dsa);
            }
        }
    }

    public static Map<Integer, Integer> getTypeMap() {
        return typeMap;
    }

    static {
        typeMap.put(67, 83);
        typeMap.put(70, 68);
        typeMap.put(82, 82);
        typeMap.put(68, 68);
        typeMap.put(73, 73);
        typeMap.put(76, 76);
        typeMap.put(83, 83);
        typeMap.put(65, 65);
        typeMap.put(80, 80);
        typeMap.put(2004, 82);
        typeMap.put(2005, 83);
        typeMap.put(1, 73);
        typeMap.put(91, 65);
        typeMap.put(3, 68);
        typeMap.put(8, 68);
        typeMap.put(6, 68);
        typeMap.put(4, 76);
        typeMap.put(2, 68);
        typeMap.put(7, 68);
        typeMap.put(-6, 73);
        typeMap.put(92, 83);
        typeMap.put(12, 83);
    }



}

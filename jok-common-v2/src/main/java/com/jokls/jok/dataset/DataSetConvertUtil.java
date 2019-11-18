package com.jokls.jok.dataset;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 15:21
 */
public class DataSetConvertUtil {
    private static Map<String, String> filterMap = new HashMap<>();

    public static String dataSet2String(IDataset dataSet, String columnName){
        try{
            if( null == dataSet){
                return null;
            }else{
                dataSet.beforeFirst();
                if(!dataSet.hasNext()){
                    return null;
                }else{
                    dataSet.next();
                    String value = dataSet.getString(columnName);
                    return value;
                }
            }
        }catch (Exception e){
            return null;
        }
    }

    public static String[] dataSet2StringArray(IDataset dataSet, String columnName){
        try{
            String[] tmp;
            if(null == dataSet){
                return null;
            } else {
                dataSet.beforeFirst();
                if(!dataSet.hasNext()){
                    return  null;
                }else {
                    tmp = new String[dataSet.getRowCount()];

                    for(int i=0; i <  tmp.length; i++){
                        dataSet.locateLine(i+1);
                        tmp[i] = dataSet.getString(columnName);
                    }
                    return tmp;
                }
            }
        }catch (Exception e){
            return null;
        }
    }


    public static <E> E dataSet2Object(IDataset dataSet, Class<E> clazz){
        try{
            if(null == dataSet){
                return null;
            }else{
                dataSet.beforeFirst();
                if(!dataSet.hasNext()){
                    return  null;
                }else{
                    Field[] fields = clazz.getDeclaredFields();
                    Object entity = clazz.newInstance();
                    dataSet.next();

                    for (Field field : fields) {
                        boolean isFilter = filterField(field);

                        if (!isFilter) {
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            try {
                                dataSet.getString(fieldName);
                            } catch (Exception e) {
                                continue;
                            }

                            try {
                                if (!Character.class.equals(field.getType())) {
                                    if (Long.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getLong(fieldName));
                                    } else if (Integer.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Short.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Integer.TYPE.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Long.TYPE.equals(field.getType())) {
                                        field.set(entity, (long) dataSet.getInt(fieldName));
                                    } else if (Double.TYPE.equals(field.getType())) {
                                        field.set(entity, dataSet.getDouble(fieldName));
                                    } else if (byte[].class.equals(field.getType())) {
                                        field.set(entity, dataSet.getByteArray(fieldName));
                                    } else if (Boolean.TYPE.equals(field.getType())) {
                                        String v = dataSet.getString(fieldName);
                                        if (v != null && !v.equals("") && !v.equalsIgnoreCase("f") && !v.equalsIgnoreCase("false") && !v.equals("0")) {
                                            field.set(entity, Boolean.TRUE);
                                        } else {
                                            field.set(entity, Boolean.FALSE);
                                        }
                                    } else {
                                        field.set(entity, dataSet.getString(fieldName));
                                    }

                                } else {
                                    String v = dataSet.getString(fieldName);
                                    Character charValue = null != v && !"".equals(v) ? v.charAt(0) : null;
                                    field.set(entity, charValue);
                                }
                            } catch (Exception e) {
                                if (!fieldName.equals("serialVersionUID")) {
                                    if (Character.class.equals(field.getType())) {
                                        field.set(entity, ' ');
                                    } else if (Integer.class.equals(field.getType())) {
                                        field.set(entity, 0);
                                    } else if (Short.class.equals(field.getType())) {
                                        field.set(entity, 0);
                                    } else {
                                        field.set(entity, null);
                                    }
                                }
                            }

                        }
                    }
                    return (E)entity;
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <E> List<E> datSet2ObjectList(IDataset dataSet, Class<E> clazz){
        if(dataSet != null){
            try{
                List<E> result = new ArrayList<>();
                dataSet.beforeFirst();

                while (dataSet.hasNext()){
                    Field[] fields = clazz.getDeclaredFields();
                    E entity = clazz.newInstance();
                    dataSet.next();

                    for (Field field : fields) {
                        boolean isFilter = filterField(field);
                        if(!isFilter){
                            field.setAccessible(true);
                            String fieldName = field.getName();

                            setFieldAttribute(dataSet, entity, field, fieldName, fieldName);
                        }
                    }
                    result.add(entity);
                }
                return result;
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return new ArrayList();
            }
        }else{
            return new ArrayList<>();
        }

    }


    public static <E> E dataSet2ObjectByCamel(IDataset dataSet, Class<E> clazz){
        try {
            if(null == dataSet){
                return null;
            } else{
                List<E> result = new ArrayList<E>();
                dataSet.beforeFirst();

                if(!dataSet.hasNext()){
                    return  null;
                } else {
                    Field[] fields = clazz.getDeclaredFields();
                    E entity = clazz.newInstance();
                    dataSet.next();
                    for (Field field : fields) {

                        boolean isFilter = filterField(field);
                        if(!isFilter){
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            try {
                                dataSet.getString(fieldName);
                            } catch (Exception e) {
                                continue;
                            }

                            try {
                                String v;
                                if (!Character.class.equals(field.getType())) {
                                    if (Long.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getLong(fieldName));
                                    } else if (Integer.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Short.class.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Integer.TYPE.equals(field.getType())) {
                                        field.set(entity, dataSet.getInt(fieldName));
                                    } else if (Long.TYPE.equals(field.getType())) {
                                        field.set(entity, (long)dataSet.getInt(fieldName));
                                    } else if (Double.TYPE.equals(field.getType())) {
                                        field.set(entity, dataSet.getDouble(fieldName));
                                    } else if (byte[].class.equals(field.getType())) {
                                        field.set(entity, (byte[])dataSet.getByteArray(fieldName));
                                    } else if (Boolean.TYPE.equals(field.getType())) {
                                        v = dataSet.getString(fieldName);
                                        if (v != null && !v.equals("") && !v.equalsIgnoreCase("f") && !v.equalsIgnoreCase("false") && !v.equals("0")) {
                                            field.set(entity, Boolean.TRUE);
                                        } else {
                                            field.set(entity, Boolean.FALSE);
                                        }
                                    } else {
                                        field.set(entity, dataSet.getString(fieldName));
                                    }
                                } else {
                                    v = dataSet.getString(fieldName);
                                    Character charValue = null != v && !"".equals(v) ? v.charAt(0) : null;
                                    field.set(entity, charValue);
                                }
                            } catch (Exception e) {
                                if (!fieldName.equals("serialVersionUID")) {
                                    if (Character.class.equals(field.getType())) {
                                        field.set(entity, ' ');
                                    } else if (Integer.class.equals(field.getType())) {
                                        field.set(entity, 0);
                                    } else if (Short.class.equals(field.getType())) {
                                        field.set(entity, 0);
                                    } else {
                                        field.set(entity, null);
                                    }
                                }
                            }
                        }
                    }

                    return entity;
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <E> List<E> dataSet2ObjectListByCamel(IDataset dataSet, Class<E> clazz){
        if(dataSet != null){
            try{
                List<E> result = new ArrayList<>();
                dataSet.beforeFirst();

                while(dataSet.hasNext()){
                    Field[] fields = clazz.getDeclaredFields();
                    E entity = clazz.newInstance();
                    dataSet.next();;

                    for(Field field : fields){
                        boolean isFilter = filterField(field);
                        if(!isFilter){
                            field.setAccessible(true);
                            String fieldName = field.getName();
                            String columnName = fieldName2ColumnName(fieldName);

                            setFieldAttribute(dataSet, entity, field, fieldName, columnName);
                        }
                    }
                    result.add(entity);
                }
                return result;
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }else{
            return new ArrayList<>();
        }
    }

    private static <E> void setFieldAttribute(IDataset dataSet, E entity, Field field, String fieldName, String columnName) {
        try{
            String v;
            if (!Character.class.equals(field.getType())) {
                if (Long.class.equals(field.getType())) {
                    field.set(entity, dataSet.getLong(columnName));
                } else if (Integer.class.equals(field.getType())) {
                    field.set(entity, dataSet.getInt(columnName));
                } else if (Short.class.equals(field.getType())) {
                    field.set(entity, dataSet.getInt(columnName));
                } else if (Integer.TYPE.equals(field.getType())) {
                    field.set(entity, dataSet.getInt(columnName));
                } else if (Long.TYPE.equals(field.getType())) {
                    field.set(entity, (long)dataSet.getInt(columnName));
                } else if (Double.TYPE.equals(field.getType())) {
                    field.set(entity, dataSet.getDouble(fieldName));
                } else if (byte[].class.equals(field.getType())) {
                    field.set(entity, (byte[])dataSet.getByteArray(fieldName));
                } else if (Boolean.TYPE.equals(field.getType())) {
                    v = dataSet.getString(fieldName);
                    if (v != null && !v.equals("") && !v.equalsIgnoreCase("f") && !v.equalsIgnoreCase("false") && !v.equals("0")) {
                        field.set(entity, Boolean.TRUE);
                    } else {
                        field.set(entity, Boolean.FALSE);
                    }
                } else {
                    field.set(entity, dataSet.getString(columnName));
                }
            } else {
                v = dataSet.getString(columnName);
                Character charValue = null != v && !"".equals(v) ? v.charAt(0) : null;
                field.set(entity, charValue);
            }
        } catch (Exception e) {
        }
    }


    public static boolean filterField(Field field) {
        if (null == field) {
            return true;
        } else {
            String fieldName = field.getName();
            return filterMap.containsKey(fieldName);
        }
    }

    public static String fieldName2ColumnName(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            StringBuilder buffer = new StringBuilder(strLen);
            for(int i = 0; i < strLen; ++i) {
                char ch = str.charAt(i);
                if (Character.isUpperCase(ch)) {
                    ch = Character.toLowerCase(ch);
                    buffer.append("_");
                }

                if (Character.isDigit(ch)) {
                    buffer.append("_");
                }

                buffer.append(ch);
            }

            return buffer.toString();
        } else {
            return str;
        }
    }
}

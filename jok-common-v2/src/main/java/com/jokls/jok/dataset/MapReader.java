package com.jokls.jok.dataset;

import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.event.field.Field;
import com.jokls.jok.event.field.FieldCreator;
import com.jokls.jok.event.field.FieldValue;
import com.jokls.jok.util.IndexMap;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:15
 */
public class MapReader {
    private IDataset dataset = null;
    private IndexMap<FieldValue> values = new IndexMap();
    private FieldCreator fc;
    private IDatasetAttribute dsa;

    public MapReader(IDataset ds){
        this.dataset = ds;
        this.dsa = DatasetService.getDefaultDatasetAttribute();
        this.fc = FieldCreator.getNewInstance(DatasetService.getDefaultDatasetAttribute());
        this.trans2Map();
    }

    private void trans2Map() {
        if(this.dataset != null && this.dataset.getRowCount() > 0){
            this.dataset.locateLine(1);
            int colCount = this.dataset.getTotalCount();

            for(int i = 0 ; i <= colCount; i++){
                String colName = this.dataset.getColumnName(i);
                Object value = this.dataset.getValue(i);
                FieldValue fv = this.fc.getFieldValue(value);
                this.values.put(colName, fv);
            }
        }
    }

    public int getInt(String colName) {
        FieldValue fv = this.values.get(colName);
        return fv != null ? fv.getInt(): this.dsa.getDefInt();
    }

    public int getInt(String colName, int def){
        FieldValue fv = this.values.get(colName);
        if(fv != null){
            try{
                return fv.getInt(def);
            }catch (Exception e){
            }
        }

        return def;
    }

    public long getLong(String columnName){
        FieldValue fv = this.values.get(columnName);
        return fv != null ? fv.getLong() : this.dsa.getDefLong();
    }

    public long getLong(String colName, long def){
        FieldValue fv = this.values.get(colName);
        if(fv != null){
            return fv.getLong(def);
        }

        return def;
    }

    public double getDouble(String columnName){
        FieldValue fv = this.values.get(columnName);
        return fv != null ? fv.getDouble() : this.dsa.getDefDouble();
    }

    public double getDouble(String columnName, double def){
        FieldValue fv = this.values.get(columnName);
        if(fv != null){
            return fv.getDouble(def);
        }

        return def;
    }

    public String[] getStringArray(String columanName){
        FieldValue fv = this.values.get(columanName);
        return fv != null ? fv.getStringArray() : this.dsa.getDefStrings();
    }

    public String[] getStringArray(String colName,String[] def){
        FieldValue fv = this.values.get(colName);
        if(fv != null){
            return fv.getStringArray(def);
        }

        return def;
    }

    public Object getValue(String colName){
        FieldValue fv = this.values.get(colName);
        return fv != null ? fv.getValue() : null;
    }

    public Object getValue(String colName,Object def){
        FieldValue fv = this.values.get(colName);
        return fv != null ? fv.getValue():def;
    }



}

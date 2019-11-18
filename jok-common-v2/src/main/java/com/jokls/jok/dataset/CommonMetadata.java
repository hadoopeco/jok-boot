package com.jokls.jok.dataset;

import com.jokls.jok.event.field.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 17:04
 */
public class CommonMetadata implements IDatasetMetaData, Cloneable {
    protected ArrayList<Field> fields = new ArrayList<>();
    protected Map<String, Integer> mapping = new HashMap<>();

    public CommonMetadata() {
    }

    public int addField(Field field){
        this.fields.add(field);
        int index = this.findColumn(field.getName());
        if( index == 0){
            this.mapping.put(field.getName(), this.fields.size() - 1);
        }

        return this.fields.size();
    }

    @Override
    public int getColumnCount() {
        return this.fields.size();
    }



    @Override
    public int findColumn(String columnName) {
        int index = 0;
        if(columnName != null && columnName.length() != 0){
            Integer it = this.mapping.get(columnName);
            if(it == null){
                index = 0;
            } else {
                index = it + 1;
            }

        }
        return index;
    }

    @Override
    public String getColumnName(int column) {
        return !this.isValidColumnIndex(column) ? null : this.fields.get(column -1).getName();
    }

    @Override
    public char getColumnType(int column) {
        return !this.isValidColumnIndex(column) ? 'N' : (this.fields.get(column -1)).getType();
    }

    public boolean isValidColumnIndex(int column) { return  column > 0 && column <= this.fields.size();}

    public Field getField(int column){
        return this.isValidColumnIndex(column) ? this.fields.get(column -1 ): null;
    }

    protected void clear(){
        this.fields.clear();
        this.mapping.clear();
    }

    public CommonMetadata clone(){
        CommonMetadata other ;
        try{
            other = (CommonMetadata)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        other.mapping = new HashMap<>(this.mapping.size());
        other.mapping.putAll(this.mapping);
        int size = this.fields.size();

        other.fields = new ArrayList<>(size);
        ArrayList<Field> newFields = other.fields;
        ArrayList<Field> oldFields = this.fields;

        for(int i=0; i<size ; i++){
            newFields.add(oldFields.get(i).clone());
        }
        return other;
    }

    public String toString() {
        ArrayList<Field> oldFields = this.fields;
        int size = oldFields.size();
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(int i = 0; i < size; ++i) {
            sb.append(oldFields.get(i).toString());
            if (i != size - 1) {
                sb.append(",");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}

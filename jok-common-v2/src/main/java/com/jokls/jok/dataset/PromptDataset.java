package com.jokls.jok.dataset;

import com.jokls.jok.event.field.Field;
import com.jokls.jok.event.field.FieldCreator;
import com.jokls.jok.event.field.FieldValue;
import com.jokls.jok.exception.DatasetRuntimeException;
import com.jokls.jok.exception.EventRuntimeException;

import java.util.Arrays;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 17:13
 */
public class PromptDataset implements IDataset, Cloneable{
    private CommonMetadata metadata = new CommonMetadata();
    private String dsName;
    private int totalCount = -1;
    private final FieldCreator fc;
    private int workMode = 1;
    private FieldValue defaultFieldValue;
    private IDatasetAttribute dssa;
    private int rowCount = 0;
    private int currentLineIndex = 1;
    private int colCount = 0;
    private int colCapacity = 50;
    private int rowCapacity = 10;
    private transient FieldValue[] values;

    protected PromptDataset(IDatasetAttribute dssa) {
        this.dssa = dssa;
        this.fc = FieldCreator.getNewInstance(dssa);
        this.defaultFieldValue = this.fc.getFieldValue((Object)null);
        this.values = new FieldValue[this.colCapacity * this.rowCapacity];
    }

    protected PromptDataset(int minColCapacity, int minRowCapacity, IDatasetAttribute dssa) {
        this.dssa = dssa;
        this.fc = FieldCreator.getNewInstance(dssa);
        this.defaultFieldValue = this.fc.getFieldValue((Object)null);
        this.colCapacity = minColCapacity;
        this.rowCapacity = minRowCapacity;
        this.values = new FieldValue[this.colCapacity * this.rowCapacity];
    }

    public PromptDataset clone() {
        PromptDataset newDS = new PromptDataset(this.colCapacity, this.rowCapacity, this.dssa);
        newDS.metadata = this.metadata.clone();
        newDS.rowCount = this.rowCount;
        newDS.colCount = this.colCount;
        newDS.dsName = this.dsName;
        newDS.totalCount = this.totalCount;
        newDS.workMode = this.workMode;
        System.arraycopy(this.values, 0, newDS.values, 0, this.values.length);
        return newDS;
    }

    public void ensureCapacity(int minColcapacity, int minRowCapacity){
        this.ensureColCapacity(minColcapacity);
        this.ensureRowCapacity(minRowCapacity);
    }

    private void ensureColCapacity(int minCapacity){
        int oldCapacity = this.colCapacity;
        if(oldCapacity < minCapacity){
            int newCapacity = oldCapacity * 3 /2 +1;
            if(newCapacity < minCapacity){
                newCapacity = minCapacity;
            }

            int totalCapacity = this.rowCapacity * newCapacity;
            FieldValue[] oldValues = this.values;
            this.values = new FieldValue[totalCapacity];

            for(int i = 0 ; i < this.rowCount; i++){
                System.arraycopy(oldValues, i* oldCapacity, this.values, i* newCapacity, oldCapacity);
            }
            oldValues = null;
        }
    }

    private void ensureRowCapacity(int minCapacity){
        int oldCapacity = this.rowCapacity;
        if (oldCapacity < minCapacity) {
            int newCapacity = oldCapacity * 3 / 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            int totalCapacity = this.colCapacity * newCapacity;
            FieldValue[] oldValues = this.values;
            this.values = new FieldValue[totalCapacity];
            System.arraycopy(oldValues, 0, this.values, 0, oldValues.length);
            oldValues = null;
        }
    }

    @Override
    public String getDatasetName() {
        return this.dsName;
    }

    @Override
    public void setDatasetName(String name) {
        this.dsName = name;
    }

    @Override
    public int getTotalCount() {
        return this.totalCount;
    }

    @Override
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public IDatasetMetaData getMetaData() {
        return this.metadata;
    }

    @Override
    public void deleteRow(int rowIndex) {
        this.checkRowIndex(rowIndex);

        if(rowIndex != this.rowCount){
            System.arraycopy(this.values, rowIndex * this.colCapacity, this.values, (rowIndex - 1 )* this.colCapacity, this.colCapacity);
        }

        Arrays.fill(this.values, (rowIndex - 1) * this.colCapacity, rowIndex * this.colCapacity - 1, null);
        --this.rowCount;

    }

    private void checkRowIndex(int rowIndex){
        if(rowIndex < 1 || rowIndex > this.rowCount){
            throw  new DatasetRuntimeException("65", rowIndex, 1, this.rowCount);
        }
    }

    @Override
    public int getColumnCount() {
        return this.colCount;
    }

    @Override
    public char getColumnType(int column) {
        return this.metadata.getColumnType(column);
    }

    @Override
    public int findColumn(String columnName) {
        return this.metadata.findColumn(columnName);
    }

    @Override
    public String getColumnName(int column) {
        return this.metadata.getColumnName(column);
    }

    @Override
    public int getInt(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        int def = this.dssa.getDefInt();
        return this.getInt(fv, def);
    }

    @Override
    public int getInt(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        int def = this.dssa.getDefInt();
        return this.getInt(fv, def);
    }

    public int getInt(int columnIndex, int def) {
        FieldValue fv = this.getFieldValue(columnIndex);
        return this.getInt(fv, def);
    }

    private int getInt(FieldValue fv, int def) {
        try {
            return fv.getInt(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public int getInt(String columnName, int def) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getInt(fv, def);
    }



    @Override
    public long getLong(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        long def = this.dssa.getDefLong();
        return this.getLong(fv, def);
    }

    @Override
    public long getLong(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        long def = this.dssa.getDefLong();
        return this.getLong(fv, def);
    }

    @Override
    public long getLong(String columnName, long def) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getLong(fv, def);
    }

    @Override
    public long getLong(int columnIndex, long def) {
        FieldValue fv =this.getFieldValue(columnIndex);
        return this.getLong(fv, def);
    }

    private long getLong(FieldValue fv, long def){
        try {
            return fv.getLong(def);
        }catch (DatasetRuntimeException e){
            if(this.isExceptionMode()){
                throw e;
            }else {
                return def;
            }
        }
    }

    @Override
    public double getDouble(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        double def = this.dssa.getDefDouble();
        return this.getDouble(fv, def);
    }

    @Override
    public double getDouble(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        double def = this.dssa.getDefDouble();
        return this.getDouble(fv, def);
    }

    @Override
    public double getDouble(String columnName, double def) {
        FieldValue fv =this.getFieldValue(columnName);
        return this.getDouble(fv, def);
    }

    @Override
    public double getDouble(int columnIndex, double def) {
        FieldValue fv =this.getFieldValue(columnIndex);
        return this.getDouble(fv, def);
    }

    private double getDouble(FieldValue fv, double def) {
        try {
            return fv.getDouble(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public byte[] getByteArray(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        byte[] def = this.dssa.getDefBytes();
        return this.getByteArray(fv, def);
    }

    @Override
    public byte[] getByteArray(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        byte[] def = this.dssa.getDefBytes();
        return this.getByteArray(fv, def);
    }

    @Override
    public byte[] getByteArray(String columnName, byte[] def) {
        FieldValue fv =this.getFieldValue(columnName);
        return this.getByteArray(fv, def);
    }

    @Override
    public byte[] getByteArray(int columnIndex, byte[] def) {
        FieldValue fv =this.getFieldValue(columnIndex);
        return this.getByteArray(fv, def);
    }

    private byte[] getByteArray(FieldValue fv, byte[] def) {
        try {
            return fv.getByteArray(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public String getString(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        String def = this.dssa.getDefString();
        return this.getString(fv, def);
    }

    @Override
    public String getString(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        String def = this.dssa.getDefString();
        return this.getString(fv, def);
    }

    @Override
    public String getString(String columnName, String def) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getString(fv, def);
    }

    @Override
    public String getString(int columnIndex, String def) {
        FieldValue fv =this.getFieldValue(columnIndex);
        return this.getString(fv, def);
    }

    private String getString(FieldValue fv, String def) {
        try {
            return fv.getString(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public String[] getStringArray(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        String[] def = this.dssa.getDefStrings();
        return this.getStrings(fv, def);
    }

    @Override
    public String[] getStringArray(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        String[] def = this.dssa.getDefStrings();
        return this.getStrings(fv, def);
    }

    @Override
    public String[] getStringArray(String columnName, String[] def) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getStrings(fv, def);
    }

    private String[] getStrings(FieldValue fv, String[] def) {
        try {
            return fv.getStringArray(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public IDataset getSubDataset(int columnIndex) {
        return (IDataset) this.getValue(columnIndex);
    }

    @Override
    public IDataset getSubDataset(String columnName) {
        return (IDataset) this.getValue(columnName);
    }

    @Override
    public String[] getStringArray(int columnIndex, String[] def) {
        FieldValue fv = this.getFieldValue(columnIndex);
        return this.getStrings(fv, def);
    }

    @Override
    public Object getValue(String columnName) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getValue(fv, null);
    }

    @Override
    public Object getValue(int columnIndex) {
        FieldValue fv = this.getFieldValue(columnIndex);
        return this.getValue(fv, null);
    }

    @Override
    public Object getValue(String columnName, Object def) {
        FieldValue fv = this.getFieldValue(columnName);
        return this.getValue(fv, def);
    }

    @Override
    public Object getValue(int columnIndex, Object def) {
        FieldValue fv = this.getFieldValue(columnIndex);
        return this.getValue(fv, def);
    }

    private Object getValue(FieldValue fv, Object def) {
        try {
            return fv.getValue();
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    @Override
    public void locateLine(int lineIndex) {
        this.checkRowIndex(lineIndex);
        this.currentLineIndex = lineIndex;

    }

    @Override
    public void beforeFirst() {
        this.currentLineIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return this.currentLineIndex < this.rowCount;
    }

    @Override
    public void next() {
        ++this.currentLineIndex;
    }

    @Override
    public int getMode() {
        return this.workMode;
    }

    @Override
    public void setMode(int mode) {
        if(MODE_EXCEPTION == mode){
            this.workMode = mode;
        }else{
            this.workMode = MODE_DEFAULT;
        }
    }

    @Override
    public int getRowCount() {
        return this.rowCount;
    }

    @Override
    public void addColumn(String colName) {
        this.addColumn(colName, 83);
    }

    @Override
    public void addColumn(String colName, int type) {
        this.ensureColCapacity(this.colCount + 1);
        Field field = this.fc.getField(colName, type);
        this.metadata.addField(field);
        ++this.colCount;
    }

    @Override
    public void modifyColumnType(String colName, int type) throws DatasetRuntimeException {
        Integer t = FieldCreator.getTypeMap().get(type);
        if (t == null) {
            throw new EventRuntimeException("69", type);
        } else {
            int index = this.checkColumnName(colName, true);
            Field field = this.metadata.getField(index);
            field.setType((char)t.intValue());
        }
    }

    @Override
    public void modifyColumnType(int colIndex, int type) throws DatasetRuntimeException {
        Integer t = FieldCreator.getTypeMap().get(type);
        if (t == null) {
            throw new EventRuntimeException("69", type);
        } else {
            this.checkColumnIndex(colIndex);
            Field field = this.metadata.getField(colIndex);
            field.setType((char)t.intValue());
        }
    }

    private void updateFieldValue(FieldValue fv, int rowIndex, int colIndex) {
        this.checkRowIndex(rowIndex);
        this.checkColumnIndex(colIndex);
        --rowIndex;
        --colIndex;
        this.values[rowIndex * this.rowCapacity + colIndex] = fv;
    }

    private void updateFieldValue(FieldValue fv, int rowIndex, String colName) {
        this.checkRowIndex(rowIndex);
        int colIndex = this.checkColumnName(colName, this.isExceptionMode());
        if (colIndex != 0) {
            --rowIndex;
            --colIndex;
            this.values[rowIndex * this.rowCapacity + colIndex] = fv;
        }
    }

    private void updateFieldValue(FieldValue fv, int colIndex) {
        this.updateFieldValue(fv, this.currentLineIndex, colIndex);
    }

    private void updateFieldValue(FieldValue fv, String colName) {
        this.updateFieldValue(fv, this.currentLineIndex, colName);
    }

    @Override
    public void updateByteArray(int columnIndex, byte[] v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateByteArray(String columnName, byte[] v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public void updateSubDataset(int columnIndex, IDataset v) throws DatasetRuntimeException {
        this.updateValue(columnIndex, v);
    }

    @Override
    public void updateSubDataset(String columnName, IDataset v) throws DatasetRuntimeException {
        this.updateValue(columnName, v);
    }

    @Override
    public void updateDouble(int columnIndex, double v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateDouble(String columnName, double v) throws DatasetRuntimeException {
        FieldValue fv =this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public void updateInt(int columnIndex, int v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateInt(String columnIndex, int v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateLong(int columnIndex, long v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateLong(String columnName, long v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public void updateString(int columnIndex, String v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateString(String columnName, String v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public void updateStringArray(int columnIndex, String[] v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateStringArray(String columnName, String[] v) throws DatasetRuntimeException {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public void updateValue(int columnIndex, Object v) {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnIndex);
    }

    @Override
    public void updateValue(String columnName, Object v) {
        FieldValue fv = this.fc.getFieldValue(v);
        this.updateFieldValue(fv, columnName);
    }

    @Override
    public boolean appendRow() {
        this.ensureRowCapacity(this.rowCount + 1);
        ++this.rowCount;
        this.currentLineIndex = this.rowCount;
        return true;
    }

    @Override
    public void clear() {
        this.rowCount = 0;
        this.colCount = 0;
        this.currentLineIndex = 0;
        if (this.values != null) {
            Arrays.fill(this.values, (Object)null);
        }
    }

    private FieldValue getFieldValue(int colIndex) {
        return this.getFieldValue(this.currentLineIndex, colIndex);
    }

    private FieldValue getFieldValue(String colName) {
        return this.getFieldValue(this.currentLineIndex, colName);
    }

    private FieldValue getFieldValue(int rowIndex, String colName) {
        this.checkRowIndex(rowIndex);
        int colIndex = this.checkColumnName(colName, this.isExceptionMode());
        if (colIndex == 0) {
            return this.defaultFieldValue;
        } else {
            --rowIndex;
            --colIndex;
            FieldValue value = this.values[rowIndex * this.rowCapacity + colIndex];
            if (value == null) {
                value = this.defaultFieldValue;
            }

            return value;
        }
    }

    private FieldValue getFieldValue(int rowIndex, int colIndex) {
        this.checkRowIndex(rowIndex);
        this.checkColumnIndex(colIndex);
        --rowIndex;
        --colIndex;
        return this.values[rowIndex * this.rowCapacity + colIndex];
    }

    private int checkColumnName(String columnName, boolean bException) {
        int index = this.metadata.findColumn(columnName);
        if (index == 0 && bException) {
            throw new DatasetRuntimeException("68", columnName);
        } else {
            return index;
        }
    }

    private void checkColumnIndex(int columnIndex) {
        if (columnIndex < 1 || columnIndex > this.colCount) {
            throw new DatasetRuntimeException("66", columnIndex, 1, this.colCount);
        }
    }

    protected boolean isExceptionMode() {
        return 0 == this.getMode();
    }

    @Override
    public void clearAll() {
        this.metadata.clear();
        this.clear();
    }
}

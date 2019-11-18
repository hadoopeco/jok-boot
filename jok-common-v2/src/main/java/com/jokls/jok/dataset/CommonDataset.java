package com.jokls.jok.dataset;

import com.jokls.jok.event.field.Field;
import com.jokls.jok.event.field.FieldCreator;
import com.jokls.jok.event.field.FieldValue;
import com.jokls.jok.exception.DatasetRuntimeException;
import com.jokls.jok.exception.EventRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 17:04
 */
public class CommonDataset implements IDataset, Cloneable{
    //todo: need add more method
    private CommonMetadata metadata = new CommonMetadata();
    private List<ArrayList<FieldValue>> lines = new ArrayList<>();
    private int currentLineIndex = 1;
    private ArrayList<FieldValue> currentLine;
    private String datasetName;
    private IDatasetAttribute dsa;
    private int totalCount = -1;
    private  FieldCreator fc ;
    private int workMode = 1;
    private FieldValue defaultFieldValue;
    private ArrayList<FieldValue> _tmpBak;

    public CommonDataset(IDatasetAttribute dsa) {
        this.dsa = dsa;
        this.fc = FieldCreator.getNewInstance(dsa);
        this.defaultFieldValue = this.fc.getFieldValue((Object)null);
    }

    public CommonDataset clone(){
        CommonDataset newDs;

        try{
            newDs = (CommonDataset) super.clone();
        }catch (CloneNotSupportedException e){
            throw new InternalError();
        }

        newDs.metadata = this.metadata.clone();
        List<ArrayList<FieldValue>> oldLines = this.lines;
        int rowCount  = oldLines.size();
        newDs.lines = new ArrayList<>(rowCount);
        List<ArrayList<FieldValue>> newLines = newDs.lines;

        for (ArrayList<FieldValue> line : oldLines) {
            ArrayList<FieldValue> oldLine = line;
            ArrayList<FieldValue> newLine = (ArrayList<FieldValue>) oldLine.clone();
            newLines.add(newLine);
        }

        return newDs;
    }

    public CommonDataset cloneInclude(String[] cols){
        CommonDataset newDs = new CommonDataset(this.dsa);
        newDs.datasetName = this.datasetName;
        newDs.totalCount = this.totalCount;
        newDs.workMode = this.workMode;
        int len = cols.length;
        Map<String, Integer> mapping = this.metadata.mapping;
        ArrayList<Field> fields = (ArrayList<Field>)this.metadata.fields.clone();

        for(String colName : cols){
            Integer index = mapping.get(colName);
            if(index != null) {
                fields.set(index, null);
            }
        }

        int pastColCount = fields.size();
        Integer[] indexs = new Integer[pastColCount];


        int marked = 0;
        for(int rownum = 0; rownum< pastColCount; rownum++){
            Field field = fields.get(rownum);
            if(field != null){
                marked++;
                indexs[rownum]  = rownum + 1;
                newDs.addColumn(field.getName(),this.metadata.getColumnType(rownum+1));
            }else{
                indexs[rownum] = 0;
            }
        }

        int rowCount  = this.lines.size();
        if(marked !=0 && rowCount != 0){
            ArrayList<FieldValue> currentLine;
            ArrayList<FieldValue> newLine;

            int m;
            if(rowCount <=2 && marked <= 10){
                for (ArrayList<FieldValue> line : this.lines) {
                    newDs.appendRow();
                    newLine = newDs.currentLine;
                    currentLine = line;
                    m = 0;
                    for (Integer index : indexs) {
                        if (index > 0) {
                            newLine.set(m++, currentLine.get(index - 1));
                        }
                    }
                }
            } else {
              newDs._fastAppendRowBegin();

                for (ArrayList<FieldValue> line : this.lines) {
                    newLine = newDs._fastAppendRow();
                    currentLine = line;
                    m = 0;
                    for (Integer index : indexs) {
                        if (index > 0) {
                            newLine.set(m++, currentLine.get(index - 1));
                        }
                    }

                }
                newDs._fastAppendRowEnd();
            }
        }
        return newDs;
    }

    public CommonDataset cloneExclude(String[] cols){
        CommonDataset newDs = new CommonDataset(this.dsa);
        newDs.datasetName = this.datasetName;
        newDs.totalCount = this.totalCount;
        newDs.workMode = this.workMode;
        Map<String, Integer> mapping = this.metadata.mapping;
        ArrayList<Field> fields = (ArrayList<Field>) this.metadata.fields.clone();
        for(String colName :cols){
            Integer index  = mapping.get(colName);
            if( index != null ){
                fields.set(index, null);
            }
        }

        int pastColCount = fields.size();
        Integer[] indexs = new Integer[pastColCount];

        int marked = 0;
        for(int rownum= 0; rownum< pastColCount; rownum++){
          Field field =  fields.get(rownum);
          if(field != null){
              ++marked;
              indexs[rownum] = rownum +1;
              newDs.addColumn(field.getName(), this.metadata.getColumnType(rownum + 1));
          } else {
              indexs[rownum] = 0;
          }
        }

        int rowCount = this.lines.size();
        if(marked !=0 && rowCount != 0){
            ArrayList<FieldValue> newLine ;
            ArrayList currentLine;

            if(rowCount <= 2 && marked <= 10 ){
                for (ArrayList<FieldValue> line : this.lines) {
                    newDs.appendRow();
                    newLine = newDs.currentLine;
                    currentLine = line;

                    int m = 0;
                    for (Integer index : indexs) {
                        if (index > 0) {
                            newLine.set(m++, (FieldValue)currentLine.get(index - 1));
                        }
                    }
                }
            } else {
              newDs._fastAppendRowBegin();

                for (ArrayList<FieldValue> line : this.lines) {
                    newLine = newDs._fastAppendRow();
                    currentLine = line;
                    int m = 0;

                    for (Integer index : indexs) {
                        if (index > 0) {
                            newLine.set(m++, (FieldValue)currentLine.get(index - 1));
                        }
                    }

                }
                newDs._fastAppendRowEnd();
            }
        }
        return newDs;

    }

    @Override
    public String getDatasetName() {
        return this.datasetName;
    }

    @Override
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
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
        return this;
    }

    @Override
    public void deleteRow(int rowIndex) {
        this.checkRowIndex(rowIndex);
        this.lines.remove(rowIndex - 1);
    }



    public IDatasetAttribute getDatasetAttribute() {
        return this.dsa;
    }

    public IDatasetAttribute setDatasetAttribute(IDatasetAttribute dssa){
        if( dssa != null){
            this.dsa.copyFrom(dssa);
        }
        return dssa;
    }

    public void clear(){
        this.metadata.clear();
        this.lines.clear();
        this.currentLineIndex = 1;
        this.currentLine = null;
    }

    @Override
    public void clearAll() {
        this.metadata.clear();
        this.lines.clear();
        this.currentLineIndex = 1;
        this.currentLine = null;
    }

    @Override
    public int getInt(String clolumnName) {
        return 0;
    }

    @Override
    public int getInt(int clolumnIndex) {
        return 0;
    }

    @Override
    public int getInt(String clolumnName, int def) {
        return 0;
    }

    @Override
    public int getInt(int clolumnIndex, int def) {
        return 0;
    }

    @Override
    public long getLong(String clolumnName) {
        return 0;
    }

    @Override
    public long getLong(int clolumnIndex) {
        return 0;
    }

    @Override
    public long getLong(String clolumnName, long def) {
        return 0;
    }

    @Override
    public long getLong(int columnIndex, long def) {
        this.checkColumnIndex(columnIndex);
        return this._getLong(columnIndex, def);

    }

    @Override
    public double getDouble(String clolumnName) {
        return 0;
    }

    @Override
    public double getDouble(int clolumnIndex) {
        return 0;
    }

    @Override
    public double getDouble(String clolumnName, double def) {
        return 0;
    }

    @Override
    public double getDouble(int clolumnIndex, double def) {
        return 0;
    }

    @Override
    public byte[] getByteArray(String clolumnName) {
        return new byte[0];
    }

    @Override
    public byte[] getByteArray(int clolumnIndex) {
        return new byte[0];
    }

    @Override
    public byte[] getByteArray(String clolumnName, byte[] def) {
        return new byte[0];
    }

    @Override
    public byte[] getByteArray(int clolumnIndex, byte[] def) {
        return new byte[0];
    }

    @Override
    public String getString(String clolumnName) {
        return null;
    }

    @Override
    public String getString(int clolumnIndex) {
        return null;
    }

    @Override
    public String getString(String clolumnName, String def) {
        return null;
    }

    @Override
    public String getString(int clolumnIndex, String def) {
        return null;
    }

    @Override
    public String[] getStringArray(String clolumnName) {
        return new String[0];
    }

    @Override
    public String[] getStringArray(int clolumnIndex) {
        return new String[0];
    }

    @Override
    public String[] getStringArray(String clolumnName, String[] def) {
        return new String[0];
    }

    @Override
    public IDataset getSubDataset(int clolumnIndex) {
        return null;
    }

    @Override
    public IDataset getSubDataset(String clolumnName) {
        return null;
    }

    @Override
    public String[] getStringArray(int clolumnIndex, String[] def) {
        return new String[0];
    }

    @Override
    public Object getValue(String clolumnName) {
        return null;
    }

    @Override
    public Object getValue(int index) {
        return this.getValue(index, null);
    }

    @Override
    public Object getValue(String columnName, Object def) {
        int index = this.checkColumnName(columnName, this.isExceptionMode());
        return index == 0 ? def : this._getValue(index, def);
    }

    @Override
    public Object getValue(int clolumnIndex, Object def) {
        return null;
    }

    public void locateLine(int lineIndex){
        if( !this.locateRow(lineIndex) ){
            throw new DatasetRuntimeException("65", lineIndex, 1, this.lines.size());
        }
    }

    @Override
    public void beforeFirst() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public void next() {

    }

    protected boolean locateRow(int rowIndex){
        if( rowIndex > 1 && rowIndex <= this.getRowCount()){
            this.currentLineIndex = rowIndex;
            this.currentLine = this.lines.get(this.currentLineIndex - 1);
            return true;
        } else {
            return false;
        }
    }

    public int getRowCount() {
         return this.lines.size();
    }

    @Override
    public void addColumn(String colName) {
        this.addColumn(colName, 83);
    }

    public void addColumn(String colName, int type){
        Field filed = this.fc.getField(colName, type);
        int realIndex = this.metadata.addField(filed);
        int size = this.lines.size();

        if(size > 0){
            List<FieldValue> vs = this.lines.get(0);
            if(realIndex > vs.size()){
                for (ArrayList<FieldValue> line : this.lines) {
                    FieldValue fv = this.fc.getFieldValue((Object) null);
                    line.add(fv);
                }
            }
        }
    }

    @Override
    public void modifyColumnType(String colName, int type) throws DatasetRuntimeException {
        Integer t = FieldCreator.getTypeMap().get(type);
        if(t != null){
            int index = this.checkColumnName(colName, true);
            Field field = this.metadata.getField(index);
            field.setType((char)t.intValue());
        }

    }

    @Override
    public void modifyColumnType(int colIndex, int type) throws DatasetRuntimeException {
        Integer t =  FieldCreator.getTypeMap().get(type);
        if( null != t){
            this.checkColumnIndex(colIndex);
            Field field = this.metadata.getField(colIndex);
            field.setType((char)t.intValue());
        }else{
            throw new EventRuntimeException("69" , type);
        }
    }

    @Override
    public void updateByteArray(int colIndex, byte[] v) throws DatasetRuntimeException {
        this.checkColumnIndex(colIndex);
        this._updateByteArray(colIndex,v);
    }

    @Override
    public void updateByteArray(String colName, byte[] v) throws DatasetRuntimeException {
        int colIndex = this.checkColumnName(colName,true);
        this._updateByteArray(colIndex, v);
    }

    private void _updateByteArray(int columnIndex, byte[] v) throws DatasetRuntimeException {
        Field field = this.metadata.getField(columnIndex);
        if (field.getType() != 'R') {
            throw new DatasetRuntimeException("the column type and value type does not match, adjust the type to byte[]!", new Object[0]);
        } else {
            FieldValue value = this.fc.getFieldValue(v);
            this.currentLine.set(columnIndex - 1, value);
        }
    }


    @Override
    public void updateSubDataset(int columnIndex, IDataset v) throws DatasetRuntimeException {
        this._updateValue(columnIndex, v);
    }

    @Override
    public void updateSubDataset(String columnName, IDataset v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateValue(columnIndex, v);
    }

    @Override
    public void updateDouble(int columnIndex, double v) throws DatasetRuntimeException {
        this.checkColumnIndex(columnIndex);
        this._updateDouble(columnIndex, v);
    }

    @Override
    public void updateDouble(String columnName, double v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateValue(columnIndex, v);
    }

    private void _updateDouble(int columnIndex, double v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateInt(int columnIndex, int v) throws DatasetRuntimeException {
        this.checkColumnIndex(columnIndex);
        this._updateInt(columnIndex, v);
    }

    @Override
    public void updateInt(String columnName, int v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateInt(columnIndex, v);
    }

    private void _updateInt(int columnIndex, int v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateLong(int columnIndex, long v) throws DatasetRuntimeException {
        this.checkColumnIndex(columnIndex);
        this._updateLong(columnIndex, v);
    }

    @Override
    public void updateLong(String columnName, long v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateLong(columnIndex, v);
    }

    private void _updateLong(int columnIndex, long v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateString(int columnIndex, String v) throws DatasetRuntimeException {
        this.checkColumnIndex(columnIndex);
        this._updateString(columnIndex, v);
    }

    @Override
    public void updateString(String columnName, String v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateString(columnIndex, v);
    }

    private void _updateString(int columnIndex, String v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateStringArray(int columnIndex, String[] v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateStringArray(String columnName, String[] v) throws DatasetRuntimeException {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateStringArray(columnIndex, v);
    }

    private void _updateStringArray(int columnIndex, String[] v) throws DatasetRuntimeException {
        FieldValue value = this.fc.getFieldValue(v);
        this.currentLine.set(columnIndex - 1, value);
    }

    @Override
    public void updateValue(int columnIndex, Object v) {
        this.checkColumnIndex(columnIndex);
        this._updateValue(columnIndex, v);
    }

    @Override
    public void updateValue(String columnName, Object v) {
        int columnIndex = this.checkColumnName(columnName, true);
        this._updateValue(columnIndex, v);
    }

    private void _updateValue(int columnIndex, Object v) {
        Field field = this.metadata.getField(columnIndex);
        if (v instanceof byte[] && field.getType() != 'R') {
            throw new DatasetRuntimeException("the column type and value type does not match, adjust the type to byte[]!");
        } else {
            FieldValue value = this.fc.getFieldValue(v);
            int type = this.metadata.getColumnType(columnIndex);
            if ((type == 'L' || type == 'I') && value.getType() == 'D') {
                this.metadata.getField(columnIndex).setType('D');
            }

            this.currentLine.set(columnIndex - 1, value);
        }
    }


    public boolean appendRow(){

        int size = this.metadata.getColumnCount();
        this.currentLine = new ArrayList<>(size);
        this.lines.add(this.currentLine);

        for(int i= 1; i <= size; i++){
            this.currentLine.add(this.defaultFieldValue);
        }
        return false;
    }


    private void _fastAppendRowBegin(){
        int size = this.metadata.getColumnCount();
        this._tmpBak = new ArrayList<>(size);
        for (int i = 1; i<= size; ++i){
            this._tmpBak.add(this.defaultFieldValue);
        }
    }

    private ArrayList<FieldValue> _fastAppendRow(){
        this.currentLine = (ArrayList<FieldValue>) this._tmpBak.clone();
        this.lines.add(this.currentLine);
        return this.currentLine;
    }

    private void _fastAppendRowEnd(){
        this._tmpBak = null;
    }

    protected void checkColumnIndex(int columnIndex) {
        if (!this.metadata.isValidColumnIndex(columnIndex)) {
            throw new DatasetRuntimeException("66", columnIndex, 1, this.metadata.getColumnCount());
        }
    }



    private void checkRowIndex(int rowIndex) {
        int rowCount = this.lines.size();
        if (rowIndex < 1 || rowIndex > rowCount) {
            throw new DatasetRuntimeException("65", rowIndex, 1, rowCount);
        }
    }

    public int getCurrentLineIndex() {
        return this.currentLineIndex;
    }

    @Override
    public int getColumnCount() {
        return this.metadata.getColumnCount();
    }

    @Override
    public char getColumnType(int columnIndex) {
        return this.metadata.getColumnType(columnIndex);
    }

    @Override
    public int findColumn(String columnName) {
        return this.metadata.findColumn(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.metadata.getColumnName(columnIndex);
    }


    public int getMode() {
        return this.workMode;
    }

    @Override
    public void setMode(int mode) {
        if( MODE_EXCEPTION == mode){
            this.workMode = mode;
        } else {
            this.workMode = MODE_DEFAULT;
        }
    }


    protected boolean isExceptionMode() {
        return 0 == this.getMode();
    }

    protected int checkColumnName(String columnName, boolean bException) {
        int index = this.metadata.findColumn(columnName);
        if (index == 0 && bException) {
            throw new DatasetRuntimeException("68", columnName);
        } else {
            return index;
        }
    }

    private Object _getValue(int columnIndex, Object def) {
        FieldValue value = this.currentLine.get(columnIndex - 1);

        try {
            return value.getValue();
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }

    private long _getLong(int columnIndex, long def) {
        FieldValue value = this.currentLine.get(columnIndex - 1);

        try {
            return value.getLong(def);
        } catch (DatasetRuntimeException e) {
            if (this.isExceptionMode()) {
                throw e;
            } else {
                return def;
            }
        }
    }
}

package com.jokls.jok.dataset;

import com.jokls.jok.dataset.writer.IMapWriter;
import com.jokls.jok.event.field.FieldCreator;
import com.jokls.jok.event.field.FieldValue;
import com.jokls.jok.exception.DatasetRuntimeException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 14:56
 */
public class MapWriter  implements IMapWriter {
    private IDataset ds;
    private FieldCreator fc;

    public MapWriter(){
        this(DatasetService.getDefaultInstance().getDataset(),DatasetService.getDefaultDatasetAttribute());
    }

    public MapWriter(IDataset ds){
        this(ds, DatasetService.getDefaultDatasetAttribute());
    }

    public MapWriter(IDataset ds, IDatasetAttribute attr){
        if (ds == null) {
            throw new DatasetRuntimeException("2", new Object[]{"dataset is null"});
        } else {
            this.ds = ds;
            this.fc = FieldCreator.getNewInstance(attr);
        }
    }

    public void put(String name, int value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateInt(name, value);
    }



    public void put(String name, long value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateLong(name, value);
    }

    public void put(String name, double value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateDouble(name, value);
    }

    public void put(String name, String value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateString(name, value);
    }

    public void put(String name, byte[] value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateByteArray(name, value);
    }

    public void put(String name, String[] value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateStringArray(name, value);
    }

    public void put(String name, Object value){
        appendRow(name, this.fc.getFieldValue(value));
        this.ds.updateValue(name, value);
    }


    private void appendRow(String name, FieldValue fieldValue) {
        if(this.ds.getRowCount() == 0){
            this.ds.appendRow();
        }

        this.ds.locateLine(1);
        FieldValue fv = fieldValue;
        if(this.ds.findColumn(name) == 0){
            this.ds.addColumn(name, fv.getType());
        }
    }

    public IDataset getDataset() {
        return this.ds;
    }
}

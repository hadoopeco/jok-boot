package com.jokls.jok.dataset;

import com.jokls.jok.dataset.convertor.ConvertorRegistry;
import com.jokls.jok.event.field.FieldCreator;
import com.jokls.jok.event.field.FieldValue;
import com.jokls.jok.exception.DatasetException;
import com.jokls.jok.exception.DatasetRuntimeException;
import com.jokls.jok.util.StringUtil;
import com.jokls.jok.util.objectutil.PropertyUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 13:44
 */
public class DatasetService {
    protected static DatasetService staticInstance;
    protected static IDatasetAttribute staticdssa = new DatasetAttribute();
    protected IDatasetAttribute dssa;
    protected FieldCreator fc;
    protected ConvertorRegistry convertorRegistry;
    protected static boolean closePrintDataset = true;

    protected DatasetService(IDatasetAttribute attr){
        if(attr == null){
            this.dssa = new DatasetAttribute();
        }else{
            this.dssa = attr;
        }
        this.fc = FieldCreator.getNewInstance(this.dssa);
        this.convertorRegistry = new ConvertorRegistry();
    }

    public static DatasetService getDefaultInstance(){return staticInstance; }

    public static DatasetService getInstance(){return new DatasetService(new DatasetAttribute());}

    public static DatasetService getInstance(IDatasetAttribute attr){return new DatasetService(attr);}

    public static IDatasetAttribute getDatasetAttribute(IDataset dataset){
        if(dataset == null){
            return null;
        } else {
            CommonDataset ds = (CommonDataset)dataset;
            return ds.getDatasetAttribute();
        }
    }

    public static IDatasetAttribute getDefaultDatasetAttribute() {
        return staticdssa;
    }


    public IDataset getDataset() {
        return this.getDataset(staticdssa);
    }

    public IDataset getDataset(IDatasetAttribute attr) {
        return new CommonDataset(attr);
    }

    public IDataset getDataset(Map oneRow) {
        return this.getDataset(oneRow, staticdssa);
    }

    public IDataset getDataset(Map oneRow, IDatasetAttribute attr) {
        if (oneRow == null) {
            return this.getDataset(attr);
        } else {
            List<Map> l = new ArrayList();
            l.add(oneRow);
            return this.getDataset(l, attr);
        }
    }

    public IDataset getDataset(Collection<Map> mutiRows) {
        return this.getDataset(mutiRows, staticdssa);
    }

    public IDataset getDataset(Collection<?> mutiRows, Class<?> clz) {
        return this.getDataset(mutiRows, clz, staticdssa);
    }

    public IDataset getDataset(Collection<Map> mutiRows, IDatasetAttribute attr){
        if (mutiRows != null && mutiRows.size() != 0) {
            Iterator<Map> it = mutiRows.iterator();
            IDataset ds = this.getDataset(attr);
            Map<String, Object> oneRow = (Map)it.next();
            if (oneRow.size() == 0) {
                return this.getDataset(attr);
            } else {
                Iterator en = oneRow.entrySet().iterator();

                Map.Entry e;
                String key;
                Object value;
                while(en.hasNext()) {
                    e = (Map.Entry)en.next();
                    key = (String)e.getKey();
                    value = e.getValue();
                    if (value != null) {
                        this.addColumnWithValue(ds, key, value);
                    }
                }

                it = mutiRows.iterator();

                while(it.hasNext()) {
                    ds.appendRow();
                    oneRow = (Map)it.next();
                    en = oneRow.entrySet().iterator();

                    while(en.hasNext()) {
                        e = (Map.Entry)en.next();
                        key = (String)e.getKey();
                        value = e.getValue();
                        if (value != null) {
                            if (ds.findColumn(key) == 0) {
                                this.addColumnWithValue(ds, key, value);
                            }

                            try {
                                this.updateRowValue(ds, key, value);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                return ds;
            }
        } else {
            return this.getDataset();
        }
    }

    public IDataset getDataset(Collection<?> mutiRows,  Class<?> clz ,IDatasetAttribute attr){
        IDataset ds;
        if(mutiRows != null && mutiRows.size() !=0){
            ds = this.getDataset(attr);
            Object obj = mutiRows.iterator().next();

            if(obj == null ){
                return ds;
            }else{
                List<String> attrs = PropertyUtils.getReadableProperties(obj);
                attrs.forEach(p ->{
                    if( !StringUtil.equals("class",p)) {
                        Object value = PropertyUtils.read(obj, p);
                        this.addColumnWithValue(ds, p, value);
                    }
                });


                Iterator mutit = mutiRows.iterator();
                while(true) {
                    Object tmpObj;
                    do {
                        if (!mutit.hasNext()) {
                            return ds;
                        }

                        tmpObj = mutit.next();
                    } while(!clz.isInstance(tmpObj));

                    ds.appendRow();

                    for (String prop : attrs) {
                        if (!StringUtil.equals("class", prop)) {
                            Object value = PropertyUtils.read(tmpObj, prop);

                            try {
                                this.updateRowValue(ds, prop, value);
                            } catch (DatasetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }else{
            ds = this.getDataset();
            if(clz != null){
                List<String> attrs = PropertyUtils.getReadableProperties(clz);
                attrs.forEach(p ->{
                    if (!StringUtil.equals("class", p)) {
                        ds.addColumn(p, 83);
                    }
                });
            }
            return ds;
        }
    }

    public IDataset getDataset(ResultSet rs) {
        return this.getDataset(rs, staticdssa);
    }

    public IDataset getDataset(ResultSet rs, IDatasetAttribute attr) {
        IDataset cds = this.getDataset(attr);

        try {
            ResultSetMetaData rsmeta = rs.getMetaData();
            int colCount = rsmeta.getColumnCount();

            int i;
            for(i = 1; i <= colCount; ++i) {
                cds.addColumn(rsmeta.getColumnName(i), rsmeta.getColumnType(i));
            }

            while(rs.next()) {
                cds.appendRow();

                for(i = 1; i <= colCount; ++i) {
                    cds.updateValue(i, rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cds;
    }

    private void addColumnWithValue(IDataset ds, String key, Object object) {
        char type = 'S';
        if (object != null) {
            FieldValue fv = this.fc.getFieldValue(object);
            type = fv.getType();
        }

        ds.addColumn(key, type);
    }

    private void updateRowValue(IDataset ds, String key, Object object) throws DatasetException {
        ds.updateValue(key, object);
    }

    public Map<String, Object> transformToMap(IDataset dataset) {
        if (dataset.getRowCount() <= 0) {
            return new HashMap<String, Object>();
        } else {
            dataset.locateLine(1);
            return this.parseDatasetToMap(dataset);
        }
    }

    public List<Map<String, Object>> transformToListMap(IDataset dataset) {
        List<Map<String, Object>> list = new ArrayList();
        int rowCount = dataset.getRowCount();
        int colCount = dataset.getColumnCount();
        if (rowCount > 0 && colCount > 0) {
            for(int i = 1; i <= rowCount; ++i) {
                dataset.locateLine(i);
                list.add(this.parseDatasetToMap(dataset));
            }

            return list;
        } else {
            return list;
        }
    }
    private Map<String, Object> parseDatasetToMap(IDataset dataset) {
        HashMap<String, Object> map = new HashMap();
        int colCount = dataset.getColumnCount();

        for(int i = 1; i <= colCount; ++i) {
            if (dataset.getColumnType(i) == 'R') {
                map.put(dataset.getColumnName(i), dataset.getByteArray(i));
            } else if (dataset.getColumnType(i) == 'D') {
                map.put(dataset.getColumnName(i), dataset.getDouble(i));
            } else if (dataset.getColumnType(i) == 'I') {
                map.put(dataset.getColumnName(i), dataset.getInt(i));
            } else if (dataset.getColumnType(i) == 'L') {
                map.put(dataset.getColumnName(i), dataset.getLong(i));
            } else if (dataset.getColumnType(i) == 'A') {
                map.put(dataset.getColumnName(i), dataset.getStringArray(i));
            } else if (dataset.getColumnType(i) == 'P') {
                IDataset subDs = dataset.getSubDataset(i);
                List<Map<String, Object>> subMap = this.transformToListMap(subDs);
                map.put(dataset.getColumnName(i), subMap);
            } else {
                map.put(dataset.getColumnName(i), dataset.getValue(i));
            }
        }

        return map;
    }

    static {
        staticInstance = new DatasetService(staticdssa);
    }
}

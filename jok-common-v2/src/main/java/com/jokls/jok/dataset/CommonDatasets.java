package com.jokls.jok.dataset;

import com.jokls.jok.exception.DatasetRuntimeException;

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
 * Date: 2019/6/14 17:04
 */
public class CommonDatasets implements IDatasets {
    private Map<String, IDataset> nameMapResult = new HashMap();
    private List<String> names = new ArrayList<>();

    public IDataset getDataset(String name){
        return this.nameMapResult.get(name);
    }

    public IDataset getDataset( int index){
        if(index >=0 && index < this.names.size()){
            String name = this.names.get(index);
            return this.nameMapResult.get(name);
        }else{
            return null;
        }
    }

    public int getDatasetCount(){return this.names.size();}

    public String getDatasetName(int index){
        return index >0 && index < this.names.size() ? this.names.get(index) : null;
    }

    public void putDataset(IDataset dataset){
        if(dataset == null){
            throw new DatasetRuntimeException("2", "input dataset is null");
        }else{
            String name = dataset.getDatasetName();
            if(name == null){
                if(this.getDatasetCount() > 0){
                    String firstName = this.getDataset(0).getDatasetName();

                    if(firstName == null){
                        throw new DatasetRuntimeException("8", "duplicate default dataset");
                    }
                }
                this.names.add(0, null);
                this.nameMapResult.put(null, dataset);
            }else{
                int index = this.names.indexOf(name);
                if(index == -1 ){
                    this.names.add(name);
                }
                dataset.setDatasetName(name);
                this.nameMapResult.put(name, dataset);
            }
        }
    }

    public void clear() {
        this.nameMapResult.clear();
        this.names.clear();
    }
}

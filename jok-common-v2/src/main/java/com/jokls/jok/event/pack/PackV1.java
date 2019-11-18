package com.jokls.jok.event.pack;

import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasetAttribute;
import com.jokls.jok.event.IPack;

import java.io.ByteArrayOutputStream;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:31
 */
public class PackV1 implements IPack {
    private String charset = "UTF-8";
    private IDataset dataset = null;
    private byte[] data = null;
    private boolean isPack = false;
    private boolean isUnpack = false;
    private static PackStatItems packStatItems = new PackStatItems();
    private static PackStatItems unpackStatItems = new PackStatItems();

    public PackV1(String charset) {
        this.setCharset(charset);
    }

    public PackV1(byte[] data, String charset) {
        this.setCharset(charset);
        this.data = data;
        this.unPack(data);
    }


    @Override
    public void addDataset(IDataset ds) {

    }

    @Override
    public boolean unPack(byte[] bytes) {
        return false;
    }

    @Override
    public byte[] Pack() {
        if (this.isPack){
            return this.data;
        } else {
           this.isPack = this.doPack();
            return this.data;
        }
    }

    @Override
    public IDataset getDataset(int index) {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public int getDatasetCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void setCharset(String charset) {

    }

    private boolean doPack() {
        long startPackTime = System.nanoTime();
        this.isPack = false;
        this.data = null;
        IDataset dataset = this.dataset;
        if (dataset == null) {
            dataset = DatasetService.getDefaultInstance().getDataset();
        }

        ByteArrayOutputStream bytebuf = new ByteArrayOutputStream();
        int col = dataset.getColumnCount();
        int row = dataset.getRowCount();

        try {
            bytebuf.write(String.valueOf(col).getBytes());
            bytebuf.write(1);
            bytebuf.write(String.valueOf(row).getBytes());
            bytebuf.write(1);

            for(int i = 1; i <= col; ++i) {
                bytebuf.write(dataset.getColumnName(i).getBytes(this.charset));
                bytebuf.write(1);
            }

            IDatasetAttribute dssa = DatasetService.getDatasetAttribute(dataset);
            dataset.beforeFirst();

            while(dataset.hasNext()) {
                dataset.next();

                for(int j = 1; j <= col; ++j) {
                    char type = dataset.getColumnType(j);
                    String name = dataset.getColumnName(j);
                    Object obj = dataset.getValue(name);
                    if (obj != null) {
                        bytebuf.write(dataset.getString(name).getBytes(this.charset));
                    } else if (type == 'I') {
                        bytebuf.write(Integer.valueOf(dssa.getDefInt()).toString().getBytes());
                    } else if (type == 'L') {
                        bytebuf.write(Long.valueOf(dssa.getDefLong()).toString().getBytes());
                    } else if (type == 'D') {
                        bytebuf.write(Double.valueOf(dssa.getDefDouble()).toString().getBytes());
                    }

                    bytebuf.write(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        this.data = bytebuf.toByteArray();
        packStatItems.packStatistics(System.nanoTime() - startPackTime, col * row);
        return true;
    }
}

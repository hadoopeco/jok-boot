package com.jokls.jok.dataset;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:15
 */
public class DatesetCoalition {
    private static DatesetCoalition dataDatesetCoalition = new DatesetCoalition();

    public DatesetCoalition() {
    }

    public static DatesetCoalition getInstance() {
        return dataDatesetCoalition;
    }

    public IDataset append(IDataset src, IDataset des) {
        int desColCount = des.getColumnCount();
        int desRowCount = des.getRowCount();

        int i;
        for(i = 1; i <= desColCount; ++i) {
            boolean same = false;

            for(int j = 1; j <= src.getColumnCount(); ++j) {
                if (src.getColumnName(j).equals(des.getColumnName(i))) {
                    same = true;
                    break;
                }
            }

            if (!same) {
                src.addColumn(des.getColumnName(i), des.getColumnType(i));
            }
        }

        for(i = 1; i <= desRowCount; ++i) {
            src.appendRow();
            des.locateLine(i);

            for(int colIndex = 1; colIndex <= desColCount; ++colIndex) {
                src.updateValue(des.getColumnName(colIndex), des.getValue(colIndex));
            }
        }

        return src;
    }

    public IDataset join(IDataset src, IDataset des) {
        int srcColCount = src.getColumnCount();
        int desColCount = des.getColumnCount();
        int desRowCount = des.getRowCount();
        int srcRowCount = src.getRowCount();
        boolean same = false;

        for(int i = 1; i <= desColCount; ++i) {
            same = false;

            int rowCount;
            for(rowCount = 1; rowCount <= srcColCount; ++rowCount) {
                if (des.getColumnName(i).equals(src.getColumnName(rowCount))) {
                    same = true;
                    break;
                }
            }

            if (!same) {
                src.addColumn(des.getColumnName(i), des.getColumnType(i));
                if (desRowCount <= srcRowCount) {
                    rowCount = desRowCount;
                } else {
                    rowCount = srcRowCount;
                }

                for(int index = 1; index <= rowCount; ++index) {
                    src.locateLine(index);
                    des.locateLine(index);
                    src.updateValue(des.getColumnName(i), des.getValue(i));
                }
            }
        }

        return src;
    }
}

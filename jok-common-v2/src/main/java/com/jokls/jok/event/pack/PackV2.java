package com.jokls.jok.event.pack;

import com.jokls.jok.common.util.StringUtils;
import com.jokls.jok.dataset.DatasetService;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasetAttribute;
import com.jokls.jok.event.IPack;
import com.jokls.jok.event.PackService;
import com.jokls.jok.util.ByteArrayStream;
import com.jokls.jok.util.ByteArrayUtil;
import com.jokls.jok.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:31
 */
public class PackV2 implements IPack {

    private static final Logger logger = LoggerFactory.getLogger(PackV2.class);

    private static final String PLATFORM_PARAMETER_DATASET_NAME = "PLATFORM_PARAMETER_DATASET_NAME";
    private static final String TOTAL_COUNT = "totalCount";
    private static int MAX_BYTES_LENGTH = 33554432;
    private ArrayList<IDataset> datasets = new ArrayList();
    private byte[] data = null;
    private boolean isPack = false;
    private boolean isUnpack = false;
    private String charset = "UTF-8";
    private static int PackType_Cres_Scale = 238;
    public static Map<String, String> useNullMap = new ConcurrentHashMap();
    public static String NULL_STRING = "$HS%1^2&3*4(8)9!@~#6/5-@$%$#";
    public static final String NULL_KEY = "null_key";
    public static final String NULL_STRING_KEY = "null_string_key";

    public PackV2(String charset) {
        this.setCharset(charset);
    }

    public PackV2(byte[] data, String charset) {
        this.data = data;
        this.setCharset(charset);
        this.unPack(data);
    }

    @Override
    public void addDataset(IDataset ds) {
        if(ds != null){
            this.datasets.add(ds);
            this.isPack = false;
        }
    }


    @Override
    public byte[] Pack() {
        if ( this.isPack ){
            return this.data;
        } else {
            ByteArrayStream tmpBuf = new ByteArrayStream();
            tmpBuf.write(33);
            tmpBuf.write(0);
            tmpBuf.write(3);
            int size = this.datasets.size();
            if(size == 0){
                this.datasets.add(DatasetService.getDefaultInstance().getDataset());
            }


            int [] totalCounts = new int[size];
            boolean isNeedAppend = false;
            int totalCount;
            for(int i = 0; i < size ; i++){
                IDataset ds = this.datasets.get(i);
                if (ds != null) {
                    if (StringUtil.equals("PLATFORM_PARAMETER_DATASET_NAME", ds.getDatasetName())) {
                        isNeedAppend = false;
                        break;
                    }

                    totalCount = ds.getTotalCount();
                    totalCounts[i] = totalCount;
                    if (totalCount != -1 && !isNeedAppend) {
                        isNeedAppend = true;
                    }
                }
            }

            if (isNeedAppend && totalCounts.length > 0){
                IDataset  parameters = DatasetService.getDefaultInstance().getDataset();
                parameters.setDatasetName(PLATFORM_PARAMETER_DATASET_NAME);
                parameters.addColumn(TOTAL_COUNT);

                for(int k : totalCounts){
                    parameters.appendRow();
                    parameters.updateInt(TOTAL_COUNT, k);
                }

                this.datasets.add(parameters);
            }

            for (IDataset dataset : this.datasets) {
                byte[] result = this.packDataset(dataset);
                tmpBuf.write(result);
            }

            this.data = tmpBuf.toByteArray();
            this.data[1] = (byte) this.datasets.size();
            this.isPack = true;
            return  this.data;
        }
    }

    private byte[] packDataset(IDataset ds){
        long startPackTime = System.nanoTime();
        ByteArrayStream tmpBuf = new ByteArrayStream();
        String datasetName = null;
        String charset = this.charset;

        try{
            datasetName = ds.getDatasetName();
            if(!StringUtils.isEmpty(datasetName)){
                tmpBuf.write(datasetName.getBytes(charset));
                tmpBuf.write(0);
            } else {
              tmpBuf.write(0);
            }

            int col = ds.getColumnCount();
            tmpBuf.write(ByteArrayUtil.intToByteArray_C(col));

            int row = ds.getRowCount();
            tmpBuf.write(ByteArrayUtil.intToByteArray_C(row));
            tmpBuf.write(ByteArrayUtil.intToByteArray_C(0));
            tmpBuf.write(ByteArrayUtil.intToByteArray_C(0));

            int i = 1;
            label1:
            while(true){
                if( i > col){
                    IDatasetAttribute dssa = DatasetService.getDatasetAttribute(ds);
                    ds.beforeFirst();

                    do {
                        if (!ds.hasNext()) {
                            break label1;
                        }
                        ds.next();
                        for (int j = 1; j <= col; ++j) {
                            String colName = ds.getColumnName(j);
                            char type = ds.getColumnType(j);
                            if (type == 'R') {
                                byte[] bytes = ds.getByteArray(colName);
                                if (bytes == null) {
                                    bytes = new byte[0];
                                }

                                tmpBuf.write(ByteArrayUtil.intToByteArray_C(bytes.length));
                                tmpBuf.write(bytes);
                                tmpBuf.write(0);
                            } else if (type == 'P') {
                                IDataset subDs = ds.getSubDataset(colName);
                                IPack subPack = PackService.getPacker(2, charset);
                                subPack.addDataset(subDs);
                                byte[] bytes = subPack.Pack();
                                if (bytes == null) {
                                    bytes = new byte[0];
                                }

                                tmpBuf.write(ByteArrayUtil.intToByteArray_C(bytes.length));
                                tmpBuf.write(bytes);
                                tmpBuf.write(0);
                            } else {
                                Object obj = ds.getValue(colName);
                                String value;
                                if (obj != null) {
                                    value = ds.getString(colName);
                                    if (value.length() != 1 || value.toCharArray()[0] != 0) {
                                        tmpBuf.write(value.getBytes(charset));
                                    }
                                } else {
                                    value = useNullMap.get(NULL_KEY);
                                    if (!StringUtils.isEmpty(value) && "true".equals(value) && type == 'S') {
                                        String nullStr = useNullMap.get(NULL_STRING_KEY);
                                        String tmpStr = StringUtils.isEmpty(nullStr) ? NULL_STRING : nullStr;
                                        tmpBuf.write(tmpStr.getBytes(charset));
                                    }
                                }

                                tmpBuf.write(0);
                            }
                        }
                    } while (true);
                }

                tmpBuf.write(ds.getColumnName(i).getBytes(charset));
                tmpBuf.write(0);
                int type = ds.getColumnType(i);

                if('P' == type){
                    tmpBuf.write(82);
                    tmpBuf.write(ByteArrayUtil.intToByteArray_C(MAX_BYTES_LENGTH));
                    tmpBuf.write(PackType_Cres_Scale);
                }else{
                    if("GBK".equalsIgnoreCase(charset) && type == 'L'){
                        tmpBuf.write(73);
                    }else{
                        tmpBuf.write(ds.getColumnType(i));
                    }
                    tmpBuf.write(ByteArrayUtil.intToByteArray_C(MAX_BYTES_LENGTH));
                    tmpBuf.write(100);
                }
                ++i;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding exception ",e);
        }

        byte[] tmp = tmpBuf.toByteArray();
        int start ;
        if(datasetName == null){
            start = tmp.length - 17;
            ByteArrayUtil.intToByteArray_C(start, tmp, 9);
        }else{
            try{
               start = tmp.length - datasetName.getBytes(charset).length - 17 ;
               ByteArrayUtil.intToByteArray_C(start, tmp, datasetName.getBytes(charset).length + 9);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

    public boolean unPack(byte[] data){
        String charset = this.charset;
        this.isUnpack = false;
        if(data == null){
            return false;
        }else {
           try{
               int datasetCount = ByteArrayUtil.byteToInt(data[1]);
               int i = 3;
               while ( i < data.length){
                   long startUnPackTime = System.nanoTime();
                   IDataset ds = DatasetService.getDefaultInstance().getDataset();
                   int nameLen = this.findDatasetName(ds, i);
                   i += nameLen;
                   int col = ByteArrayUtil.byteArrayToInt_C(data, i);
                   i += 4;
                   int row = ByteArrayUtil.byteArrayToInt_C(data, i);
                   i += 4;
                   ByteArrayUtil.byteArrayToInt_C(data, i);
                   i += 4;
                   ByteArrayUtil.byteArrayToInt_C(data, i);
                   i += 4;

                   List<Character> types = new ArrayList();

                   String colName;
                   for(int rowCount = 0; rowCount < col; ++rowCount){
                        int strLen = this.findStringLen(i);
                        try{
                            colName = new String(data, i, strLen -1 ,charset);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            colName = new String(data, i, strLen - 1);
                        }

                       i += strLen;
                       char colType = (char)data[i];
                       ++i;
                       ByteArrayUtil.byteArrayToInt_C(data, i);
                       i += 4;
                       int colScale = ByteArrayUtil.byteToInt(data[i]);
                       ++i;
                       types.add(colType);
                       if (colType == 'C') {
                           colType = 'S';
                       } else if (colType == 'I') {
                           colType = 'L';
                       } else if (colType == 'F') {
                           colType = 'D';
                       }

                       if (colType == 'R' && PackType_Cres_Scale == colScale) {
                           colType = 'P';
                       }

                       ds.addColumn(colName, colType);
                   }

                   for(int rowCount = 1; rowCount <= row; ++rowCount) {
                       ds.appendRow();

                       for(int j = 1; j <= col; ++j) {
                           char innerType = ds.getColumnType(j);
                           char type = types.get(j - 1);
                           int binLength = 0;
                           byte[] temp;
                           if ('P' == innerType) {
                               binLength = ByteArrayUtil.byteArrayToInt_C(data, i);
                               i += 4;
                               temp = new byte[binLength];
                               System.arraycopy(data, i, temp, 0, binLength);
                               i += binLength + 1;
                               IPack subPack = PackService.getPacker(temp, charset);
                               if (subPack.getDatasetCount() > 0) {
                                   IDataset subDataset = subPack.getDataset(0);
                                   ds.updateValue(j, subDataset);
                               }
                           } else if ('R' == type) {
                               binLength = ByteArrayUtil.byteArrayToInt_C(data, i);
                               i += 4;
                               temp = new byte[binLength];
                               System.arraycopy(data, i, temp, 0, binLength);
                               i += binLength + 1;
                               ds.updateByteArray(j, temp);
                           } else if ('C' == type) {
                               temp = new byte[1];
                               System.arraycopy(data, i, temp, 0, binLength);
                               i += 2;
                               ds.updateString(j, String.valueOf((char)temp[0]));
                           } else {
                               binLength = this.findStringLen(i);
                               temp = new byte[binLength - 1];
                               System.arraycopy(data, i, temp, 0, binLength - 1);
                               i += binLength;
                               ds.updateString(j, new String(temp, charset));
                           }
                       }
                   }

                   if (StringUtil.equals(PLATFORM_PARAMETER_DATASET_NAME, ds.getDatasetName())) {
                       int rowCount = ds.getRowCount();
                       if (this.datasets.size() >= rowCount) {
                           for(int k = 1; k <= rowCount; ++k) {
                               ds.locateLine(k);
                               (this.datasets.get(k - 1)).setTotalCount(ds.getInt(TOTAL_COUNT, -1));
                           }
                       }
                   } else {
                       this.datasets.add(ds);
                   }

               }

           } catch (UnsupportedEncodingException e) {
               logger.error("unPack exception",e);
           }

            this.isUnpack = true;
            this.data = data;
            return this.isUnpack;
        }
    }

    public static  byte[] charsetTrans(byte[] buf, String src, String des){
        if(buf == null){
            logger.info("buf is null");
            return null;
        }else{
            byte[] data = buf;
            ByteArrayStream bas = new ByteArrayStream(buf.length);
            try{
                bas.write(buf, 0, 3);

                int i = 3;

                while (i < data.length){
                    int namelen = findStringLen(data, i);
                    String dsName = new String(data, i, namelen, src);
                    bas.write(dsName.getBytes(des));

                    i += namelen;
                    bas.write(data, i, 16);
                    int col = ByteArrayUtil.byteArrayToInt_C(data, i);
                    i += 4;
                    int row = ByteArrayUtil.byteArrayToInt_C(data, i);
                    i += 12;
                    List<Character> types = new ArrayList<>();

                    char type;
                    for(int k =0; k < col;  k++){
                        int strlen = findStringLen(data, i);
                        String colName = new String(data, i, strlen, src);
                        bas.write(colName.getBytes(des));
                        i += strlen;
                        bas.write(data, i, 6);
                        type = (char)data[i];
                        i +=6;
                        types.add(type);
                    }

                    for(int k=0; k <= row; k++){
                        for(int j=0; j <= col; j++){
                            type = types.get(j -1 );
                            int binLength;
                            if('R' == type){
                                binLength = ByteArrayUtil.byteArrayToInt_C(data, i);
                                bas.write(data, i, 5 + binLength);
                                i += 5 + binLength;
                            }else if('C' == type){
                                bas.write(data, i, 2);
                                i += 2;
                            }else{
                                binLength = findStringLen(data, i);
                                String buff =new String(data, i, binLength, src);
                                bas.write(buff.getBytes(des));
                                i+= binLength;
                            }
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.info("unpack failed",e);
            }
            return bas.toByteArray();
        }
    }

    private int findDatasetName(IDataset ds, int offset){
        int length = this.findStringLen(offset);

        try {
            ds.setDatasetName(new String(this.data, offset, length - 1, this.charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ds.setDatasetName(new String(this.data, offset, length - 1));
        }

        return length;
    }

    private int findStringLen(int offset) {

        int length = 0;
        while (this.data[offset + length] != 0) {
            ++length;
        }

        ++length;
        return length;
    }

    private static int findStringLen(byte[] data, int offset) {
        int length = 0;
        while (data[offset + length] != 0) {
            ++length;
        }

        ++length;
        return length;
    }



    @Override
    public IDataset getDataset(int resultIndex) {
        int size = this.getDatasetCount();
        return resultIndex >= size ? null : this.datasets.get(resultIndex);
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public int getDatasetCount() {
        if (!this.isUnpack) {
            this.unPack(this.data);
        }

        return this.datasets.size();
    }

    @Override
    public void clear() {
        this.data = null;
        this.datasets.clear();
        this.isPack = false;
        this.isUnpack = false;
    }

    @Override
    public void setCharset(String charset) {
        this.charset = charset;
        if(charset == null || charset.length() == 0){
            this.charset = "UTF-8";
        }

        try {
            new String("测试".getBytes(), this.charset);
        } catch (UnsupportedEncodingException e) {
            this.charset = "UTF-8";
            e.printStackTrace();
        }
    }
}

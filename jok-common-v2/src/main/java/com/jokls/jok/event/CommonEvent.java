package com.jokls.jok.event;

import com.jokls.jok.dataset.CommonDatasets;
import com.jokls.jok.dataset.IDataset;
import com.jokls.jok.dataset.IDatasets;
import com.jokls.jok.exception.DatasetRuntimeException;
import com.jokls.jok.util.EventUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import  static  com.jokls.jok.event.AttributeType.*;
import  static  com.jokls.jok.event.EventTagdef.*;

public class CommonEvent implements IEvent {
    protected IDatasets enventBody = null;
    protected Map<String, Object> allTags = new HashMap();
    protected Map<String, Character> map2Type = new HashMap();
    protected List<String> vecTags = new ArrayList();
    private static char SPLIT = ' ';


    public CommonEvent() {
        long eventId = EventUtils.getInstance().generateEventNo();
        this.setIntegerAttributeValue(TAG_EVENT_ID, eventId);
        this.setIntegerAttributeValue(TAG_SENDERID , 8L);
    }

    @Override
    public void setServiceId(String serviceId) {
        this.setStringAttributeValue(TAG_JRES_SERVICE_ID, serviceId);
    }

    @Override
    public void setServiceAlias(String alias) {
        this.setStringAttributeValue(TAG_FUNCTION_ID , alias);
    }

    @Override
    public String getServiceId() {
        return this.getStringAttributeValue("75");
    }

    @Override
    public String getServiceAlias() {
        return this.getStringAttributeValue("5");
    }

    @Override
    public void setEventType(int type) {
        this.setIntegerAttributeValue("3",(long)type);
    }

    @Override
    public int getEventType() {
        return (int)this.getIntegerAttributeValue("3");
    }

    @Override
    public boolean isTimestampOn() {
        return this.hasAttribute("52");
    }

    @Override
    public void markTimestamp(boolean on) {
        if(!this.isTimestampOn() && on){
            this.setStringArrayAttributeValue("52", new String[0]);
        }else  if(this.isTimestampOn() && !on){
            this.removeAttribute("52");
        }
    }

    @Override
    public void addTimestamp(long time, String timestampInfo) {
        if(this.isTimestampOn()){
            String[] stamps = this.getStringArrayAttributeValue("52");
            if(stamps != null){
                int length = stamps.length;
                String[] newStamps = new String[length + 1];
                System.arraycopy(stamps, 0, newStamps, 0, length);
                StringBuilder sb = new StringBuilder(512);
                sb.append(time);
                sb.append(SPLIT);
                sb.append(timestampInfo);
                newStamps[length] = sb.toString();
                this.setStringArrayAttributeValue("52", newStamps);
            }
        }
    }


    @Override
    public String[] getTimestamp() {
        return this.isTimestampOn() ? this.getStringArrayAttributeValue("52") : new String[0];
    }

    @Override
    public int changeToresponse() {
        long eventType = this.getIntegerAttributeValue("3");
        if(eventType == 2L){
            eventType = 3L;
        }else{
            if(eventType != 0L){
                return 1;
            }

            eventType = 1L;
        }
        this.setIntegerAttributeValue(EventTagdef.TAG_EVENT_TYPE, eventType);
        if(this.hasAttribute(EventTagdef.TAG_SUB_SYSTEM_NO)){
            this.setIntegerAttributeValue(EventTagdef.TAG_TTL, 16L);
        }

        String[] sendpath = this.getStringArrayAttributeValue("73");
        this.setStringArrayAttributeValue("74",  sendpath);
        this.removeAttribute("73");
        if(this.hasAttribute("10")){
            String[] cresSenderPath = this.getStringArrayAttributeValue("10");
            this.setStringArrayAttributeValue("9", cresSenderPath);
            this.removeAttribute("10");
        }

        this.enventBody = new CommonDatasets();
        return 0;
    }

    @Override
    public IDatasets getEventDatas() {
        return this.enventBody;
    }

    @Override
    public void putEventData(IDataset dataset) {
        if( this.enventBody == null){
            this.enventBody = new CommonDatasets();
        }

        this.enventBody.putDataset(dataset);
    }

    @Override
    public void putEventDatas(IDatasets datasets) {
        if(this.enventBody == null){
            this.enventBody = new CommonDatasets();
        }

        int size = datasets.getDatasetCount();
        for(int i=0; i < size ; i++){
            this.enventBody.putDataset(datasets.getDataset(i));
        }
    }

    @Override
    public void setReturnCode(int returnCode) {
        this.setIntegerAttributeValue("7", returnCode);
    }

    @Override
    public int getReturnCode() {
        return (int)this.getIntegerAttributeValue("7");
    }

    @Override
    public void setErrorCode(String errorNo, String errorInfo) {
        if(!this.hasAttribute("7")){
            Integer returnCode = this.getAttributeValue("7");
            if(returnCode != null && returnCode ==0 ){
                this.setIntegerAttributeValue("7", -1L);
            }
        }

        this.setStringAttributeValue("19", errorNo);
        this.setStringAttributeValue("20", errorInfo);
    }

    @Override
    public String getErrorNo() {
        char type = this.getAttributeType(EventTagdef.TAG_ERROR_NO);
        if(AttributeType.INTEGER == type){
            return "" + this.getIntegerAttributeValue(EventTagdef.TAG_ERROR_NO);
        } else {
            if(AttributeType.STRING == type){
                String errorNo = this.getStringAttributeValue(EventTagdef.TAG_ERROR_NO);
                if (errorNo != null){
                    return errorNo;
                }
            }
            return "0";
        }
    }

    @Override
    public String getErrorInfo() {
        return this.getStringAttributeValue("20");
    }

    @Override
    public int getAttributeCount() {
        return this.vecTags.size();
    }

    @Override
    public String getAttributeName(int index) {
        return this.vecTags.get(index);
    }

    @Override
    public char getAttributeType(String name) {
        Character type = this.map2Type.get(name);
        return type != null ? type : '\u0000';
    }

    @Override
    public boolean hasAttribute(String tagName) {
        return this.vecTags.indexOf(tagName) != -1;
    }

    @Override
    public void setIntegerAttributeValue(String tagName, long value) {
        int index = this.vecTags.indexOf(tagName);
        if (index >= 0) {
            Character type = this.map2Type.get(tagName);
            if (type != null && type != 'I') {
                this.removeAttribute(tagName);
            }
        }

        this.vecTags.add(tagName);
        this.map2Type.put(tagName, 'I');
        this.allTags.put(tagName, value);
    }

    @Override
    public long getIntegerAttributeValue(String tagName) {
        Character type = this.map2Type.get(tagName);
        if (type != null && (type == 'I' || type == 'S')) {
            try {
                return Long.valueOf(this.allTags.get(tagName).toString());
            } catch (Exception e) {
                throw new DatasetRuntimeException(e.getMessage(), e);
            }
        } else {
            return 0L;
        }
    }

    @Override
    public void setStringAttributeValue(String tagName, String value) {
        int index = this.vecTags.indexOf(tagName);
        if (index >= 0) {
            Character type = this.map2Type.get(tagName);
            if (type != null && type != 'A') {
                this.removeAttribute(tagName);
            }
        }

        this.vecTags.add(tagName);
        this.map2Type.put(tagName, 'A');
        this.allTags.put(tagName, value);
    }

    @Override
    public String getStringAttributeValue(String tagName) {
        Character type = this.map2Type.get(tagName);
        if (type != null) {
            try {
                Object tag = this.allTags.get(tagName);
                return tag == null ? null : tag.toString();
            } catch (Exception e) {
                throw new DatasetRuntimeException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void setStringArrayAttributeValue(String tagName, String[] value) {
        int index = this.vecTags.indexOf(tagName);
        if (index >= 0) {
            Character type = this.map2Type.get(tagName);
            if (type != null && type != 'S') {
                this.removeAttribute(tagName);
            }
        }

        this.vecTags.add(tagName);
        this.map2Type.put(tagName, 'S');
        this.allTags.put(tagName, value);
    }

    @Override
    public String[] getStringArrayAttributeValue(String tagName) {
        Character type = this.map2Type.get(tagName);
        if (type != null && type == 'A') {
            try {
                return (String[])this.allTags.get(tagName);
            } catch (Exception e) {
                throw new DatasetRuntimeException(e.getMessage(), e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void setByteArrayAttributeValue(String tagName, byte[] value) {
        int index = this.vecTags.indexOf(tagName);
        if(index >= 0){
            Character type = this.map2Type.get(tagName);
            if(type != null && type != BINARY ){
                this.removeAttribute(tagName);
            }
        }

        this.vecTags.add(tagName);
        this.map2Type.put(tagName,BINARY);
        this.allTags.put(tagName, value);
    }

    @Override
    public byte[] getByteArrayAttributeValue(String tagName) {
        Character type = this.map2Type.get(tagName);
        return type != null && type == BINARY ? (byte[])this.allTags.get(tagName) : null;
    }

    @Override
    public void setAttributeValue(String tagName, Object value) {
        int index = this.vecTags.indexOf(tagName);
        if(index >= 0){
            Character type = this.map2Type.get(tagName);
            if(type != null && type != 'U'){
                this.removeAttribute(tagName);
            }
        }

        this.vecTags.add(tagName);
        this.map2Type.put(tagName, 'U');
        this.allTags.put(tagName, value);

    }

    @Override
    public <T> T getAttributeValue(String tagName) {
        return (T)this.allTags.get(tagName);
    }

    @Override
    public void removeAttribute(String name) {
        int index = this.vecTags.indexOf(name);
        if (index >= 0) {
            this.vecTags.remove(index);
            this.map2Type.remove(name);
            this.allTags.remove(name);
        }
    }
}

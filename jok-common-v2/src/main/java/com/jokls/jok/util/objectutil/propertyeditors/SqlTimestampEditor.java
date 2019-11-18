package com.jokls.jok.util.objectutil.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:38
 */
public class SqlTimestampEditor extends PropertyEditorSupport {
    private boolean allowEmpty;

    public SqlTimestampEditor(){
        this(true);
    }

    public SqlTimestampEditor(boolean allowEmpty){
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text){
        if(this.allowEmpty && (text == null || text.length() == 0)){
            this.setValue(null);
        }else{
            if(text.length() <= 10){
                text = text + " 00:00:00.000000000";
            } else if(text.indexOf(84) > 0){
                text = text.replace('T', ' ');
            }

            this.setValue(Timestamp.valueOf(text));
        }
    }

    public void setValue(Object value) {
        if (value instanceof Timestamp) {
            super.setValue(value);
        } else if (value == null) {
            super.setValue(null);
        } else {
            this.setAsText(value.toString());
        }

    }

    public String getAsText() {
        Object value = this.getValue();
        return value == null ? "" : value.toString();
    }

}

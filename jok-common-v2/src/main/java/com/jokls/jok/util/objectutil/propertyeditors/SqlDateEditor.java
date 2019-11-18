package com.jokls.jok.util.objectutil.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.sql.Date;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/22 15:30
 */
public class SqlDateEditor extends PropertyEditorSupport {
    private boolean allowEmpty;

    public SqlDateEditor(){
        this(true);
    }

    public SqlDateEditor(boolean allowEmpty){
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text){
        if(this.allowEmpty || text != null && text.length() != 0){
            int i = text.indexOf(24180);
            if(i > 0) {
                text = text.replace('年', '_').replace('月','_').replace('日','_');
                if(i <= 2){
                    text = "20" + text;
                }
            }

            i = text.indexOf(84);
            if (i > 0) {
                text = text.substring(0, i);
            } else {
                i = text.indexOf(32);
                if (i > 0) {
                    text = text.substring(0, i);
                }
            }

            this.setValue(Date.valueOf(text));
        }else{
            this.setValue(null);
        }
    }

    public void setValue(Object value) {
        if (value instanceof java.util.Date) {
            super.setValue(new Date(((java.util.Date)value).getTime()));
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

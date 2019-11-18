package com.jokls.jok.util;

import com.jokls.jok.event.CommonEvent;
import com.jokls.jok.event.IEvent;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 10:12
 */
public class EventUtils {

    private static EventUtils instance = null;
    private byte[] lock = new byte[0];
    private long iMessageId = 0L;

    public static EventUtils getInstance() {return instance ;}

    private EventUtils() {this.lock = new byte[0]; }

    public long generateEventNo(){
        synchronized (this.lock){
            if(this.iMessageId == 9223372036854775807L){
                this.iMessageId = 1L;
            }else{
                ++this.iMessageId;
            }
            return this.iMessageId;
        }
    }

    public static String getEventName(IEvent event){
        if(event == null){
            return "null";
        } else {
            StringBuilder sb = new StringBuilder(100);
            sb.append(event.getStringAttributeValue("11"));
            sb.append("|");
            sb.append(event.getEventType());
            sb.append("|");
            sb.append(event.getServiceId());
            sb.append("|");
            sb.append(event.getServiceAlias());
            return sb.toString();
        }
    }

    public static byte[] packEvent(IEvent event, String charset) {
        if(event == null){
            throw new IllegalArgumentException("event is null");
        }else{
            if(charset == null){
                charset = "utf-8";
            }

            if(event instanceof CommonEvent){
                CommonEvent commonEvent = (CommonEvent) event;
            }
            return null;
        }
    }


}

package com.jokls.jok.db.core.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/25 11:42
 */
public class DsLocationFactory {

    private final static Logger logger = LoggerFactory.getLogger(DsLocationFactory.class);

    private Map<String, DsLocationable> locationables = new HashMap<>();
    private static DsLocationFactory INSTANCE = new DsLocationFactory();

    public static DsLocationFactory getInstance(){return INSTANCE;}

    public DsLocationable getDSLocationable(String className){
        DsLocationable locationable = this.locationables.get(className);
        if(locationable == null){
            synchronized (this.locationables){
                locationable = this.locationables.get(className);
                if(locationable == null){
                    try{
                        locationable = (DsLocationable) Class.forName(className).newInstance();
                        this.locationables.put(className, locationable);
                    } catch (Exception  e){
                        logger.error("create instance error", e);
                    }
                }
            }
        }
        return locationable;
    }

}

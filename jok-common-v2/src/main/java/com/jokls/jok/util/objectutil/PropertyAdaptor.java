package com.jokls.jok.util.objectutil;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/24 10:44
 */
public class PropertyAdaptor {
    private String propertyName;
    private Class propertyType;
    private Method readMethod;
    private Method writeMethod;

    PropertyAdaptor(String propertyName, Class propertyType, Method readMethod, Method writeMethod){
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

    public String getReadMethodName(){
        return this.readMethod == null ? null : this.readMethod.getName();
    }

    public String getWriteMethodName(){
        return this.writeMethod == null ? null : this.writeMethod.getName();
    }

    public String getPropertyName(){
        return this.propertyName;
    }
    public Class getPropertyType(){
        return this.propertyType;
    }

    public void write(Object target, Object value){
        if( this.writeMethod == null){
            throw new RuntimeException(target + " Property " + this.propertyName + " of object "+ target + " is read-only");
        }else {
            try{
                this.writeMethod.invoke(target, value);
            }catch (Exception e){
                throw new RuntimeException(target + "Unable to update property " + this.propertyName + " of object " + target + ": " + e, e);
            }

        }

    }

    public void smartWrite(Object target, String value){
        Object convertedValue = this.convertValueForAssignment(target, value);
        this.write(target, convertedValue);
    }

    private Object convertValueForAssignment(Object target, String value){
        if(value != null && !this.propertyType.isInstance(value)){
            PropertyEditor e = PropertyEditorManager.findEditor(this.propertyType);

            if( e == null ){
                Object convertedValue = this.instantiateViaStringConstructor(target, value);
                if(convertedValue != null){
                    return convertedValue;
                } else{
                    throw new RuntimeException(" There has no Editor for property " + this.propertyName + " of class " + target.getClass());
                }
            }else{
                try{
                    e.setAsText(value);
                    return e.getValue();
                }catch (Exception ex){
                    throw new RuntimeException(" Unable to convert value [" + value +"] to type + [" + this.propertyType + "] for property ["+ this.propertyName + "] of" + target, ex);
                }
            }

        }else{
            return value;
        }
    }

    private Object instantiateViaStringConstructor(Object object, String value){
        try{
            Constructor c = this.propertyType.getConstructor(String.class);
            return c.newInstance(value);
        }catch (Exception e){
            return null;
        }
    }

    public Object read(Object target){
        if(this.readMethod== null){
            throw  new RuntimeException(target +" property " + this.propertyName + " of object" + target + " is write-only");
        }else{
            try{
                return this.readMethod.invoke(target, null);
            }catch (Exception e){
                throw new RuntimeException("Fail to read property "+ this.propertyName + " of "+ target, e);
            }
        }
    }

    public boolean isWritable(){
        return this.writeMethod != null;
    }

    public boolean isReadable(){
        return this.readMethod != null;
    }

}

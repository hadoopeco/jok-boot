package com.jokls.jok.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/12 18:07
 */
public class ClassUtils {
    private final static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);

    public static List<String> scanInterface(String[] basePackages, Class<? extends Annotation> annotation){
        List<String> classes = new ArrayList<>();
        try{
            for( String basePackage : basePackages){
                if( !StringUtils.isEmpty(basePackage)){
                    String packageSearchPath = "classpath*:"+ SpringClassUtils.convertResourcePathToClassName(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/**/*.class" ;
                    Resource[] resources = resolver.getResources(packageSearchPath);

                    for(Resource resource : resources){
                        if(resource.isReadable()){
                            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            ClassMetadata classMetadata = metadataReader.getClassMetadata();

                            if(classMetadata.isInterface() && (new AnnotationTypeFilter(annotation)).match(metadataReader, metadataReaderFactory)){
                                String className = classMetadata.getClassName();
                                if (! classes.contains(className)) {
                                    classes.add(className);
                                }
                            }
                        }

                    }
                }
            }

            return classes;
        } catch (IOException e) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }



    }

    public static Resource[] scanFile(String name) {
        try {
            return resolver.getResources("classpath*:" + name);
        } catch (IOException e) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
    }


    public static boolean isWrapClass(Class<?> clazz) {
        try {
            return ((Class)clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            logger.error("isWrapClass ", e);
            return false;
        }
    }
}
